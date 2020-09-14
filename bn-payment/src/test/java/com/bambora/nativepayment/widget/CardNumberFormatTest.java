package com.bambora.nativepayment.widget;

import com.bambora.nativepayment.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CardNumberFormatTest {

    private CardNumberFormat cardNumberFormat;
    private CharSequence VisaCard1="4242 4242 4242 4242";
    private CharSequence VisaCard2="4111 1111 1111 1111";
    private CharSequence MasterCard="5123456789012346";
    private CharSequence invalidCard="123456789";
    private CharSequence AmexCard="342345678901238";
    private CharSequence DinersCard="36876543210125";

    public static final String VALIDATION_PATTERN =
            "^(?:4[0-9]{12}(?:[0-9]{3})?" +         // Visa
                    "|(?:5[1-5][0-9]{2}" +               // MasterCard
                    "|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}" +
                    "|3[47][0-9]{13}" +                  // American Express
                    "|3(?:0[0-5]|[68][0-9])[0-9]{11}" +  // Diners Club
                    "|6(?:011|5[0-9]{2})[0-9]{12}" +     // Discover
                    "|(?:2131|1800|35\\d{3})\\d{11}" +   // JCB
                    ")$";

    @Before
    public void setup() {
        cardNumberFormat = new CardNumberFormat();
    }

    @Test
    public void updateCardTypeTest() {
        Assert.assertTrue(cardNumberFormat.updateCardType(VisaCard1));
        Assert.assertFalse(cardNumberFormat.updateCardType(VisaCard2));
        Assert.assertTrue(cardNumberFormat.updateCardType(MasterCard));
        Assert.assertTrue(cardNumberFormat.updateCardType(invalidCard));
        Assert.assertTrue(cardNumberFormat.updateCardType(DinersCard));
    }

    @Test
    public void cardTypeTest() {
        cardNumberFormat.updateCardType(AmexCard);
        Assert.assertEquals(CardNumberFormat.CardType.AMERICAN_EXPRESS,cardNumberFormat.getCardType());
        Assert.assertEquals(new Integer(R.drawable.ic_amex),cardNumberFormat.getIconResId());
        Assert.assertEquals(17,cardNumberFormat.getMaxInputLength());
        Assert.assertEquals(Arrays.asList(4, 6, 5),cardNumberFormat.getFormatGroupSizes());
    }

    @Test
    public void validationStringTest() {
        Assert.assertEquals(VALIDATION_PATTERN,cardNumberFormat.getValidationString());
    }

}
