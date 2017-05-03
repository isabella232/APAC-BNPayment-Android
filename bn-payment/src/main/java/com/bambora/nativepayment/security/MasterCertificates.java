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

package com.bambora.nativepayment.security;

import com.bambora.nativepayment.utils.CertificateUtils;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 */
public class MasterCertificates {

    private static final String[] MASTER_CERTIFICATES = {

            "-----BEGIN CERTIFICATE-----\n" +
            "MIIJnDCCBYSgAwIBAgIJANYgr/aZ63rcMA0GCSqGSIb3DQEBCwUAMFsxCzAJBgNV\n" +
            "BAYTAlNFMRIwEAYDVQQHDAlTdG9ja2hvbG0xEDAOBgNVBAoMB0JhbWJvcmExJjAk\n" +
            "BgNVBAMMHUJhbWJvcmEgSW50ZXJuYWwgUm9vdCBDQSAyMDE2MB4XDTE2MDUxNzA5\n" +
            "MDkyNFoXDTM2MDUxNzA5MDkyNFowWzELMAkGA1UEBhMCU0UxEjAQBgNVBAcMCVN0\n" +
            "b2NraG9sbTEQMA4GA1UECgwHQmFtYm9yYTEmMCQGA1UEAwwdQmFtYm9yYSBJbnRl\n" +
            "cm5hbCBSb290IENBIDIwMTYwggQiMA0GCSqGSIb3DQEBAQUAA4IEDwAwggQKAoIE\n" +
            "AQDJFEMgbuNgKrLloeD8z60MsDVwf3A/lErYldo8YjTXnWmZKdVVjzXoxNjeH4NF\n" +
            "jjERe+AcwYD/sPNGUbk1Qc/co08qdxKNvG99RGVwUJmtlh7EQBAw2Cn579FxTOR0\n" +
            "mjxfCNcbhqfC8CbSNGUS1+t3ATP4DiWcw6vaDY7j+m3nZ+QhgupBa+RNn7KX3BjN\n" +
            "+xBdGj76YWjUJOZj+AKXSlV3/nMa6OD1mv2YRGPlmjP6uCh8HHcHuGaJUMj5MHGq\n" +
            "Ghq9bFohham9nHhiA3N63SAvSxI8UP97Fl2LNrF0VnK6yWFxCR/Eav6T3MLuKshn\n" +
            "YGsqx/QEg/GdBpieH+SDOEbKMC09DgxzkZ1tnofV33cJHCvC27HbvXgbl34wYxrI\n" +
            "jLMP4n7uA6UPdtSydkvhgd/aZ0kfqGd/4W7aR7vbQrSRD8vhqBX8TJxL63COEAiQ\n" +
            "MuN/f3KnyQlyhPPXDh0BljlEYJ+uYW6pwfF0NQO+8vTxgZh+RZETH7rmDHuLp7am\n" +
            "FYKrHsaFPnd/xtgX9q+Y8DEplW+Z5v5pEftC+N63I3sp0BffdQ+aVCaaM9aMTuKz\n" +
            "dRg7Y9VAR+5wvdDaOl0HyZ00cCbgXP6NpAGwsV7Us5uhTGt1SThEkCfgRbtzbs4s\n" +
            "X+vEPLG1u0q0czAecfpx3NYerQNzSRQJc/iFqW4cONXNLy+b24n+BWvj8ZmXbpsN\n" +
            "0teyVXvnm5Q+p5IG+N6TGj9a1nfeLk7j52s/82jnDOHmu73TgNLX1munQb2SFRJ+\n" +
            "xCecfSYwbyhQkzy+byWkbtSMJZkqYinxTA+ekS2lGYi4gq/NLxQvK/GNmgAWS9kq\n" +
            "LReKLu4TR7BiCk2qwI0oCTQZai6OG/R/IL4dCbNnbrN7GMtSFbbRsjARohZtc6EH\n" +
            "MV1bDXT72zNKEX+shcjoERhumKgn21mTWJ1saXtTpgrx2J+NkuHNPoYZcAuvDBpv\n" +
            "I/vgrcwX6da/A6D7yBbFm5Exr7mMJJXlhF8TlFrOWZ0Bd5DLiYjy39pvISnhQVs1\n" +
            "PYARGIb7EmMDYGRJt3esSbQ7FELr19U4tveAq3SrAnZqYMU1fLozp1XjvBq/EQaZ\n" +
            "sejnSWbKgQYhm+JzceoZo0wH6+xhncT4xfQIf13V1zpzg0d+vx7Fym2UY/+IaBS2\n" +
            "lK2Bf7RWrQAFjE6h4AE7eeMxQbjESlzTXum6F4/Rn/SUcNqHs4MBtZQG3LI+/uDq\n" +
            "axo7htKCXlMQw70c2Ea5MmpXCC3OnGLXEjn5ES10Ot7W0/4nAdbCPIEzImJV1gg0\n" +
            "eywvBtzU3qKV7Wgi3UxCxguat7f+aVoU++Gx4cftiZjptx0adMSmUbEjcn4VG81T\n" +
            "yO4KaxLiqkqa+iwJgtJIzD4PAgMBAAGjYzBhMB0GA1UdDgQWBBR6I/4CHATD/Ek8\n" +
            "zGah7/wwFuSKvDAfBgNVHSMEGDAWgBR6I/4CHATD/Ek8zGah7/wwFuSKvDAPBgNV\n" +
            "HRMBAf8EBTADAQH/MA4GA1UdDwEB/wQEAwIBhjANBgkqhkiG9w0BAQsFAAOCBAEA\n" +
            "amxWsp4YTVs6rOGF9XvwvieCRBK7Dy0+ddf7I0tt6KgnY4j2j2jeGc7NgWI8BU+p\n" +
            "mqjyHoEzpBQJnCztmsUs0rlJp/7ZS8HmKHt+/fqboCha+OC1/mkRSwS2BZQhQGCH\n" +
            "BvzhxGmsfMun1W9+pHjCZlD4ICNDm7dk3x0OR0nmI044ydxPwhP+FJOFcCjcsJ3N\n" +
            "PVjWwY0JybFGqX4f2+rPoGlxkF9A1NNhTSkkS/MLquiv8v0zUuiw5cp/3PTmGWCh\n" +
            "YCEg6/e5E9eLrGUi8OSBilCMeVTZSUvIZPuB5yoKLUSnMH+SOvGkMvTs6uaglT8k\n" +
            "6vXgqcUytyo3KADC4c8RFEitJ/gGVyfkBGjRe9cwicaGPifE4FEa3R4kfPmOgUQb\n" +
            "vWU3Zun6a6lUIdnPlAPw/NWvzOB5j+qCb19nywfHED8cH+JWu0qnxcQj8CvfLhF/\n" +
            "bXH9r8PiypkS5KygOgfzYVNTISfvhghwrLTZalDE4fXOvsrqpmdtqX3ZwP/u/gZu\n" +
            "mwuoLQqGwG6sVmRF2gDDvbxxXNe202WUbtQkJ4iyKpmUG6tLZiWyc+9j1vTBHFaJ\n" +
            "jP48wr87SmukbcDh36Rk81UWAJnAw+koI0qd6n81diejsHFXaN1CCM9h32NSNgVb\n" +
            "oDefF/jtWCMHcsVH/lnxLheKn9CTjXVm4EaUe3Q7u83DNtp33tufXuUVx9i9tKlh\n" +
            "xv9q6Qxj8V2c9+FnA8M117hqoV0PGgrqyz4diZV3g5cSSTKXCQq5WKwrPgSgdp3C\n" +
            "+2E1ircfAm8Vc0MT3P+2n9TE4/8jka9Bv1uc3RE8qevWutY77nX8mce3EsegY3od\n" +
            "3m9cy+j6jd3qucBb4JLf/gmisTQqFi/5Z+yZ0EUT1Gn73jXtbBQfpUgSY8Ojp20c\n" +
            "EjjeWUG7bKq6l+gOJXAjVwf4F2Xa7Y8iCQo9sEgKtpaAMK+0Urp0SAj7VnctFpln\n" +
            "mqo6zbs2KFeBiTmqfDq2XFtVZIIiHbfZCJco2P+xm6lE/AOyJFlNjEiFBzgOnNaY\n" +
            "n0vwCwYT75FUwSUjAIPAQL7bR1nh+lKR8wTd4eQZBPBiJvy9vwDwMkPSv/+OSk/s\n" +
            "jnHUn81eVEYXSuaptMY9nOgz5woDt6aPDLSgbxp5NKsSN9aWOpmsiJmf3AO/t52e\n" +
            "rPjE36IEq34HJeDDeUua97hJfPng1eyGJDw1iZ2dT0YB40gSAa/kKOAPfb8MPMFV\n" +
            "pNW41B3R+hX0sPtzMJqQfjVpEb5NZQrCDjECKtsrjezC78sK8nWQpQxEeLQ/Cail\n" +
            "eLWDsJXxMsN7Tw4uyknREIoIKDpQzr4iI2SL1dPXquROLhQ0dU80S7FY9BqcU1pf\n" +
            "C4yTJeJpa4x3WQmOXK8ikw==\n" +
            "-----END CERTIFICATE-----",

            "-----BEGIN CERTIFICATE-----\n" +
                    "MIID5zCCAs+gAwIBAgIJAMFepyQzkC0VMA0GCSqGSIb3DQEBCwUAMIGJMQswCQYD\n" +
                    "VQQGEwJBVTEMMAoGA1UECAwDTlNXMQ8wDQYDVQQHDAZTWURORVkxDDAKBgNVBAoM\n" +
                    "A0lQUDEVMBMGA1UECwwMSVBQIFJlc3QgQVBJMREwDwYDVQQDDAhSRVNUIEFQSTEj\n" +
                    "MCEGCSqGSIb3DQEJARYUY2hlbi5saW5AYmFtYm9yYS5jb20wHhcNMTcwMzAzMDMy\n" +
                    "NDI1WhcNMTgwMzAzMDMyNDI1WjCBiTELMAkGA1UEBhMCQVUxDDAKBgNVBAgMA05T\n" +
                    "VzEPMA0GA1UEBwwGU1lETkVZMQwwCgYDVQQKDANJUFAxFTATBgNVBAsMDElQUCBS\n" +
                    "ZXN0IEFQSTERMA8GA1UEAwwIUkVTVCBBUEkxIzAhBgkqhkiG9w0BCQEWFGNoZW4u\n" +
                    "bGluQGJhbWJvcmEuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA\n" +
                    "u01ozTZy4e3Nf06nZWF5JO0SKhVlW1Wv29/LIBoEARTK9VWNqV33jo9rT6522XaA\n" +
                    "aee9fqvH6ntCWh1240J4a2GOaA4CMMIiz5nIk6tavw7JZ6RtyGp24o2nFqPtD+Ui\n" +
                    "aWWkTZgQJmf74r0whGXpDTMDdQCBgGZSdJ0V84jDpdu6ACkQvcI9XwNJd5WACoCp\n" +
                    "zgohH1+rcZVYqN5TtuF1gjIkxi9UJ8VBrz+llvTJ9t1p61PJ5Kvp3IDJRmwXv0ml\n" +
                    "58H7HzXnatBKAi/xIl7Yghb+fCEQ3OzBJZlF6fa/wGcMtEaE6SSrcedOLVY4dQQF\n" +
                    "1XLTYW2KiSqd8Aj6sD56YwIDAQABo1AwTjAdBgNVHQ4EFgQUC5+DJBdxo3F1c05s\n" +
                    "IYijw7WnXf4wHwYDVR0jBBgwFoAUC5+DJBdxo3F1c05sIYijw7WnXf4wDAYDVR0T\n" +
                    "BAUwAwEB/zANBgkqhkiG9w0BAQsFAAOCAQEAkhC5KohKz7/MNEuVFrDFztzYjO3I\n" +
                    "ZxQoa7nDc3TwO/GQUgjZ2shF+en71Ui1ejlvFylYh53f3hkX8bJ4tRt3geX4ZueW\n" +
                    "W8YKaSJvRoMjnMckIn0ZFPa3dRDwnVjd6lLFskIjbRmNlMQ3USyYoOCiRNm8ZEHQ\n" +
                    "FnhspggWzhp+xW617X9mB9chZx6zIW8RSKHTXOoaelHAO7aeLoLSN5PTwk2Iy/99\n" +
                    "xDXk4zbDODrxPa0wgNkgX/xt2v5gEyWtDyVQx7b8wR6+VcnAQHC7vYJD3/OX0LhF\n" +
                    "7/VhUyKJo76+XJoPm6gjier8ASc/Za2FWmfllPXJhxS08rYqTrEnHNU0ow==\n" +
                    "-----END CERTIFICATE-----\n"

    };

    public static List<Certificate> getMasterCertificates() {
        List<Certificate> masterCertificates = new ArrayList<>();
        for (String certificateString : MASTER_CERTIFICATES) {
            masterCertificates.add(CertificateUtils.parseCertificate(certificateString));
        }
        return masterCertificates;
    }
}
