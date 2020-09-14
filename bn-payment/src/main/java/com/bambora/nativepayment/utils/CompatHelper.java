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

package com.bambora.nativepayment.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources.Theme;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.bambora.nativepayment.R;

public class CompatHelper {

    public static Drawable getDrawable(Context context, int resId, Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getResources().getDrawable(resId, theme);
        } else {
            return context.getResources().getDrawable(resId);
        }
    }

    public static int getColor(Context context, int resId, Theme theme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(resId, theme);
        } else {
            return context.getResources().getColor(resId);
        }
    }

    public static int getCustomizedColor(Context context,String colorCode,String errorAlert){

           if(colorCode!=null && colorCode.length()>0)
           {
               if(colorCode.matches("#[A-Fa-f0-9]{6}"))
               {
                   return Color.parseColor(colorCode);
               }
               else
               {
                   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   builder.setTitle("Error")
                           .setMessage(errorAlert)
                           .setCancelable(false)
                           .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {

                               }
                           });
                   AlertDialog alertDialog=builder.create();
                   alertDialog.show();
                   return context.getResources().getColor(R.color.bn_purple);
               }
           }
           else
           {
               return context.getResources().getColor(R.color.bn_purple);
           }
      }
}
