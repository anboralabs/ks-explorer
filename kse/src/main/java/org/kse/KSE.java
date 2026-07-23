package org.kse;

import java.security.Provider;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.kse.crypto.provider.KseProvider;
import org.kse.crypto.x509.KseX500NameStyle;
import org.kse.version.Version;

public class KSE {

  public static Provider BC = new BouncyCastleProvider();
  public static Provider KSE = new KseProvider();

  static {
    // set default style for Bouncy Castle's X500Name class
    X500Name.setDefaultStyle(KseX500NameStyle.INSTANCE);

    // allow lax parsing of malformed ASN.1 integers
    System.setProperty("org.bouncycastle.asn1.allow_unsafe_integer", "true");
  }

  /**
   * Get application version.
   *
   * @return Application version
   */
  public static Version getApplicationVersion() {
    return new Version("5.6.1");
  }

}
