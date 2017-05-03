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

package com.bambora.nativepayment.widget;

import com.bambora.nativepayment.R;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static com.bambora.nativepayment.widget.CardNumberFormat.CardType.UNKNOWN;

/**
 * Class for determining card type and formatting of a card number input String.
 */
public class CardNumberFormat {

    /**
     * Regex pattern for validating card number input for all supported card types.
     */
    public static final String VALIDATION_PATTERN =
            "^(?:4[0-9]{12}(?:[0-9]{3})?" +         // Visa
            "|(?:5[1-5][0-9]{2}" +               // MasterCard
            "|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}" +
            "|3[47][0-9]{13}" +                  // American Express
            "|3(?:0[0-5]|[68][0-9])[0-9]{11}" +  // Diners Club
            "|6(?:011|5[0-9]{2})[0-9]{12}" +     // Discover
            "|(?:2131|1800|35\\d{3})\\d{11}" +   // JCB
            ")$";

    /**
     * A {@link CardType} that describes what card type should be used for formatting and
     * validating the user input.
     */
    private CardType cardType;

    /**
     * Max length of a formatted card number String (including spacing)
     */
    private int maxInputLength;

    /**
     * A {@link List<Integer>} that describes how a card number string should be formatted into
     * groups. The list contains group sizes in the order they should be displayed.
     */
    private List<Integer> formatGroupSizes;

    /**
     * Resource ID for an icon specific for the card type.
     */
    private Integer iconResId;

    /**
     * Initiates card type to unknown.
     */
    public CardNumberFormat() {
        setCardType(UNKNOWN);
    }

    /**
     * Getter for the {@link CardType} cardType.
     * @return {@link CardType}
     */
    public CardType getCardType() {
        return cardType;
    }

    /**
     * Getter for the {@link Integer} iconResId.
     * @return An icon resource ID {@link Integer}
     */
    public Integer getIconResId() {
        return iconResId;
    }

    /**
     * Getter for maxInputLength.
     * @return Max input length of a formatted card number (int)
     */
    public int getMaxInputLength() {
        return maxInputLength;
    }

    /**
     * Returns a regex pattern String for validating a card number.
     * @return {@link String}
     */
    public String getValidationString() {
        return VALIDATION_PATTERN;
    }

    /**
     * Getter for {@link List} formatGroupSizes
     * @return {@link List<Integer>} Sizes of groups to be separated by a delimiter
     */
    public List<Integer> getFormatGroupSizes() {
        return formatGroupSizes;
    }

    /**
     * Determines the {@link CardType} based on cardNumberInput and updates the card type
     * accordingly.
     *
     * @param cardNumberInput   A {@link CharSequence} with card number input. It does not have to
     *                          be a complete card number since type can be determined by the first
     *                          few digits.
     * @return True if the card type has been changed.
     */
    public boolean updateCardType(CharSequence cardNumberInput) {
        CardType oldCardType = this.cardType;
        String stripped = stripFromNonDigits(cardNumberInput);
        for (CardType newCardType : CardType.values()) {
            Pattern typePattern = Pattern.compile(newCardType.typePattern());
            if (typePattern.matcher(stripped).matches()) {
                setCardType(newCardType);
                return oldCardType != newCardType;
            }
        }
        setCardType(UNKNOWN);
        return oldCardType != UNKNOWN;
    }

    /**
     * Sets {@link CardType} and some values that are specific for each card type.
     *
     * @param cardType  A {@link CardType}
     */
    private void setCardType(CardType cardType) {
        this.cardType = cardType;

        switch (this.cardType) {
            case VISA:
                this.maxInputLength = 19;
                this.iconResId = R.drawable.ic_visa;
                this.formatGroupSizes = Arrays.asList(4, 4, 4, 4);
                break;
            case MASTERCARD:
                this.maxInputLength = 19;
                this.iconResId = R.drawable.ic_master_card;
                this.formatGroupSizes = Arrays.asList(4, 4, 4, 4);
                break;
            case AMERICAN_EXPRESS:
                this.maxInputLength = 17;
                this.iconResId = R.drawable.ic_amex;
                this.formatGroupSizes = Arrays.asList(4, 6, 5);
                break;
            case DINERS_CLUB:
                this.maxInputLength = 16;
                this.iconResId = R.drawable.ic_diners;
                this.formatGroupSizes = Arrays.asList(4, 6, 5);
                break;
            default:
                this.maxInputLength = 19;
                this.iconResId = null;
                this.formatGroupSizes = Arrays.asList(4, 4, 4, 4);
        }
    }

    private String stripFromNonDigits(CharSequence input) {
        return String.valueOf(input).replaceAll("[^\\d]", "");
    }

    /**
     * Enum with valid card types where the String value is a regex pattern to use for determining
     * card type based on the first few digits in a number string.
     */
    public enum CardType {
        VISA                ("^4\\d*"),
        MASTERCARD          ("^5[1-5]\\d*"),
        AMERICAN_EXPRESS    ("^3[47]\\d*"),
        DINERS_CLUB         ("^3(?:0[0-5]|[68])\\d*"),
        UNKNOWN             ("");

        private String typePattern;

        CardType(String typePattern) {
            this.typePattern = typePattern;
        }

        public String typePattern() {
            return this.typePattern;
        }
    }
}
