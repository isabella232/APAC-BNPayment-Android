/*
 * Copyright (c) 2016 Bambora ( http://bambora.com/ )
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.bambora.nativepayment.storage;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * This is a class responsible for saving objects tot disc.
 *
 * Created by oskarhenriksson on 15/10/15.
 */
public class FileStorage {

    public interface IOnObjectRead {
        void notifyOnObjectRead(Serializable object);
    }

    public interface IOnObjectSaved {
        void notifyOnObjectSaved();
    }

    protected void saveObjectToDisk(final Context context, final String fileName, final Serializable object, final IOnObjectSaved listener) {
        AsyncTask writeToFileTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(object);
                    objectOutputStream.close();
                    fileOutputStream.close();
                    postResultToUiThread(listener);
                    return null;
                }
                catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Failed to save object to disk. " + e);
                    return null;
                }
            }
        };
        executeAsyncTask(writeToFileTask);
    }

    protected void getObjectFromDisk(final Context context, final String fileName, final IOnObjectRead listener) {
        AsyncTask<Void, Void, Serializable> readFromFileTask = new AsyncTask<Void, Void, Serializable>() {
            @Override
            protected Serializable doInBackground(Void... voids) {
                Serializable object = null;
                try {
                    FileInputStream fileInputStream = context.openFileInput(fileName);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    object = (Serializable) objectInputStream.readObject();
                    objectInputStream.close();
                    fileInputStream.close();
                    postResultToUiThread(listener, object);
                }
                catch (IOException | ClassNotFoundException e) {
                    Log.e(getClass().getSimpleName(), "Failed to read object from disk. " + e);
                    postResultToUiThread(listener, null);
                }
                return object;
            }
        };
        executeAsyncTask(readFromFileTask);
    }

    private void executeAsyncTask(AsyncTask task){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
        else
            task.execute();
    }

    private void postResultToUiThread(final IOnObjectRead listener, final Serializable object) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.notifyOnObjectRead(object);
                }
            }
        });
    }

    private void postResultToUiThread(final IOnObjectSaved listener) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.notifyOnObjectSaved();
                }
            }
        });
    }
}
