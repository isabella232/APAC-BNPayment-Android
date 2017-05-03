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

package com.bambora.paymentdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bambora.nativepayment.handlers.BNPaymentHandler;
import com.bambora.nativepayment.managers.CreditCardManager.IOnCreditCardRead;
import com.bambora.nativepayment.models.creditcard.CreditCard;
import com.bambora.paymentdemo.adapter.CardListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CreditCardListActivity extends AppCompatActivity implements IOnCreditCardRead, OnItemClickListener {

    private List<CreditCard> creditCards = new ArrayList<>();
    private CardListAdapter creditCardListAdapter;
    private BNPaymentHandler paymentHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        paymentHandler = BNPaymentHandler.getInstance();
        initCreditCardList();
        paymentHandler.getRegisteredCreditCards(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        paymentHandler.getRegisteredCreditCards(this, this);
    }

    private void initCreditCardList() {
        ListView listView = (ListView) findViewById(R.id.lv_credit_cards);
        creditCardListAdapter = new CardListAdapter(this, creditCards);
        listView.setOnItemClickListener(this);
        listView.setAdapter(creditCardListAdapter);
    }

    @Override
    public void onCreditCardRead(List<CreditCard> creditCards) {
        this.creditCards.clear();
        if (creditCards != null) {
            this.creditCards.addAll(creditCards);
        }
        creditCardListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, CreditCardDetailsActivity.class);
        CreditCard item = (CreditCard) creditCardListAdapter.getItem(position);
        intent.putExtra(CreditCardDetailsActivity.EXTRA_CREDIT_CARD, item);
        startActivity(intent);
    }
}
