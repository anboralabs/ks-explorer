/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2023 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kse.utilities.oid;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

/**
 * Provides utility methods related to Object Identifiers.
 */
public class ObjectIdUtil {
  private static ResourceBundle res =
      ResourceBundle.getBundle("org/kse/utilities/oid/resources");

  private ObjectIdUtil() {}

  /**
   * Validate an object identifier. To be valid it must be well-formed and must
   * contain valid arcs ranges. The first arc must be 0-2, and the second arc
   * must be 0-39 where the first arc is 0-1 or 0-47 where the first arc is 2.
   *
   * @param oid Object identifier
   * @throws InvalidObjectIdException If object identifier is not valid.
   */
  public static void validate(ASN1ObjectIdentifier oid)
      throws InvalidObjectIdException {
    int[] arcs = extractArcs(oid);

    validate(arcs);
  }

  /**
   * Extract the arcs from an object identifier.
   *
   * @param oid Object identifier
   * @return Arcs
   * @throws InvalidObjectIdException If object identifier is not a '.'
   *     separated
   *                                  list of non-negative integers
   */
  public static int[] extractArcs(ASN1ObjectIdentifier oid)
      throws InvalidObjectIdException {
    String oidStr = oid.getId();

    StringTokenizer strTokCnt = new StringTokenizer(oidStr, ".", false);
    int arcCount = strTokCnt.countTokens();

    StringTokenizer strTok = new StringTokenizer(oidStr, ".", true);

    boolean expectDelimiter = false;

    int[] arcs = new int[arcCount];
    int i = 0;
    while (strTok.hasMoreTokens()) {
      String token = strTok.nextToken();

      if (expectDelimiter && (!token.equals(".") || !strTok.hasMoreTokens())) {
        throw new InvalidObjectIdException(res.getString(
            "InvalidOidNotNonNegativeIntSequence.exception.message"));
      } else if (!expectDelimiter) {
        try {
          arcs[i] = Integer.parseInt(token);

          if (arcs[i] < 0) {
            throw new InvalidObjectIdException(res.getString(
                "InvalidOidNotNonNegativeIntSequence.exception.message"));
          }

          i++;
        } catch (NumberFormatException ex) {
          throw new InvalidObjectIdException(res.getString(
              "InvalidOidNotNonNegativeIntSequence.exception.message"));
        }
      }

      expectDelimiter = !expectDelimiter;
    }

    return arcs;
  }

  private static void validate(int[] arcs) throws InvalidObjectIdException {
    if (arcs.length < 3) {
      throw new InvalidObjectIdException(
          res.getString("InvalidOidMinThreeArcsRequired.exception.message"));
    }

    int firstArc = -1;

    for (int j = 0; j < arcs.length; j++) {
      if (j == 0) {
        firstArc = arcs[0];

        if ((firstArc < 0) || (firstArc > 2)) {
          throw new InvalidObjectIdException(
              res.getString("InvalidOidFirstArc.exception.message"));
        }
      } else if (j == 1) {
        if (firstArc == 0) {
          if (arcs[j] > 39) {
            throw new InvalidObjectIdException(
                res.getString("InvalidOidFirstArcZero.exception.message"));
          }
        } else if (firstArc == 1) {
          if (arcs[j] > 39) {
            throw new InvalidObjectIdException(
                res.getString("InvalidOidFirstArcOne.exception.message"));
          }
        } else {
          if (arcs[j] > 47) {
            throw new InvalidObjectIdException(
                res.getString("InvalidOidFirstArcTwo.exception.message"));
          }
        }
      }
    }
  }

  private static Map<String, String> oidToNameMapping = new HashMap<>();

  static {
    oidToNameMapping.put("0.2.262.1.10", "Telesec");
    oidToNameMapping.put("0.2.262.1.10.0", "Extension");
    oidToNameMapping.put("0.2.262.1.10.1", "Mechanism");
    oidToNameMapping.put("0.2.262.1.10.1.0", "Authentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.1", "PasswordAuthentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.2",
                         "ProtectedPasswordAuthentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.3", "OneWayX509Authentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.4", "TwoWayX509Authentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.5", "ThreeWayX509Authentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.6", "OneWayISO9798Authentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.7", "TwoWayISO9798Authentication");
    oidToNameMapping.put("0.2.262.1.10.1.0.8", "TelekomAuthentication");
    oidToNameMapping.put("0.2.262.1.10.1.1", "Signature");
    oidToNameMapping.put("0.2.262.1.10.1.1.1", "Md4WithRSAAndISO9697");
    oidToNameMapping.put("0.2.262.1.10.1.1.2",
                         "Md4WithRSAAndTelesecSignatureStandard");
    oidToNameMapping.put("0.2.262.1.10.1.1.3", "Md5WithRSAAndISO9697");
    oidToNameMapping.put("0.2.262.1.10.1.1.4",
                         "Md5WithRSAAndTelesecSignatureStandard");
    oidToNameMapping.put("0.2.262.1.10.1.1.5",
                         "Ripemd160WithRSAAndTelekomSignatureStandard");
    oidToNameMapping.put("0.2.262.1.10.1.1.9", "HbciRsaSignature");
    oidToNameMapping.put("0.2.262.1.10.1.2", "Encryption");
    oidToNameMapping.put("0.2.262.1.10.1.2.0", "None");
    oidToNameMapping.put("0.2.262.1.10.1.2.1", "RsaTelesec");
    oidToNameMapping.put("0.2.262.1.10.1.2.2", "Des");
    oidToNameMapping.put("0.2.262.1.10.1.2.2.1", "DesECB");
    oidToNameMapping.put("0.2.262.1.10.1.2.2.2", "DesCBC");
    oidToNameMapping.put("0.2.262.1.10.1.2.2.3", "DesOFB");
    oidToNameMapping.put("0.2.262.1.10.1.2.2.4", "DesCFB8");
    oidToNameMapping.put("0.2.262.1.10.1.2.2.5", "DesCFB64");
    oidToNameMapping.put("0.2.262.1.10.1.2.3", "Des3");
    oidToNameMapping.put("0.2.262.1.10.1.2.3.1", "Des3ECB");
    oidToNameMapping.put("0.2.262.1.10.1.2.3.2", "Des3CBC");
    oidToNameMapping.put("0.2.262.1.10.1.2.3.3", "Des3OFB");
    oidToNameMapping.put("0.2.262.1.10.1.2.3.4", "Des3CFB8");
    oidToNameMapping.put("0.2.262.1.10.1.2.3.5", "Des3CFB64");
    oidToNameMapping.put("0.2.262.1.10.1.2.4", "Magenta");
    oidToNameMapping.put("0.2.262.1.10.1.2.5", "Idea");
    oidToNameMapping.put("0.2.262.1.10.1.2.5.1", "IdeaECB");
    oidToNameMapping.put("0.2.262.1.10.1.2.5.2", "IdeaCBC");
    oidToNameMapping.put("0.2.262.1.10.1.2.5.3", "IdeaOFB");
    oidToNameMapping.put("0.2.262.1.10.1.2.5.4", "IdeaCFB8");
    oidToNameMapping.put("0.2.262.1.10.1.2.5.5", "IdeaCFB64");
    oidToNameMapping.put("0.2.262.1.10.1.3", "OneWayFunction");
    oidToNameMapping.put("0.2.262.1.10.1.3.1", "Md4");
    oidToNameMapping.put("0.2.262.1.10.1.3.2", "Md5");
    oidToNameMapping.put("0.2.262.1.10.1.3.3", "SqModNX509");
    oidToNameMapping.put("0.2.262.1.10.1.3.4", "SqModNISO");
    oidToNameMapping.put("0.2.262.1.10.1.3.5", "Ripemd128");
    oidToNameMapping.put("0.2.262.1.10.1.3.6", "HashUsingBlockCipher");
    oidToNameMapping.put("0.2.262.1.10.1.3.7", "Mac");
    oidToNameMapping.put("0.2.262.1.10.1.3.8", "Ripemd160");
    oidToNameMapping.put("0.2.262.1.10.1.4", "FecFunction");
    oidToNameMapping.put("0.2.262.1.10.1.4.1", "ReedSolomon");
    oidToNameMapping.put("0.2.262.1.10.10", "Notification");
    oidToNameMapping.put("0.2.262.1.10.11", "Snmp-mibs");
    oidToNameMapping.put("0.2.262.1.10.11.1", "SecurityApplication");
    oidToNameMapping.put("0.2.262.1.10.12", "CertAndCrlExtensionDefinitions");
    oidToNameMapping.put("0.2.262.1.10.12.0", "LiabilityLimitationFlag");
    oidToNameMapping.put("0.2.262.1.10.12.1", "TelesecCertIdExt");
    oidToNameMapping.put("0.2.262.1.10.12.2", "Telesec policyIdentifier");
    oidToNameMapping.put("0.2.262.1.10.12.3", "TelesecPolicyQualifierID");
    oidToNameMapping.put("0.2.262.1.10.12.4", "TelesecCRLFilteredExt");
    oidToNameMapping.put("0.2.262.1.10.12.5", "TelesecCRLFilterExt");
    oidToNameMapping.put("0.2.262.1.10.12.6", "TelesecNamingAuthorityExt");
    oidToNameMapping.put("0.2.262.1.10.2", "Module");
    oidToNameMapping.put("0.2.262.1.10.2.0", "Algorithms");
    oidToNameMapping.put("0.2.262.1.10.2.1", "AttributeTypes");
    oidToNameMapping.put("0.2.262.1.10.2.10", "ElectronicOrder");
    oidToNameMapping.put("0.2.262.1.10.2.11",
                         "TelesecTtpAsymmetricApplication");
    oidToNameMapping.put("0.2.262.1.10.2.12", "TelesecTtpBasisApplication");
    oidToNameMapping.put("0.2.262.1.10.2.13", "TelesecTtpMessages");
    oidToNameMapping.put("0.2.262.1.10.2.14", "TelesecTtpTimeStampApplication");
    oidToNameMapping.put("0.2.262.1.10.2.2", "CertificateTypes");
    oidToNameMapping.put("0.2.262.1.10.2.3", "MessageTypes");
    oidToNameMapping.put("0.2.262.1.10.2.4", "PlProtocol");
    oidToNameMapping.put("0.2.262.1.10.2.5", "SmeAndComponentsOfSme");
    oidToNameMapping.put("0.2.262.1.10.2.6", "Fec");
    oidToNameMapping.put("0.2.262.1.10.2.7", "UsefulDefinitions");
    oidToNameMapping.put("0.2.262.1.10.2.8", "Stefiles");
    oidToNameMapping.put("0.2.262.1.10.2.9", "Sadmib");
    oidToNameMapping.put("0.2.262.1.10.3", "ObjectClass");
    oidToNameMapping.put("0.2.262.1.10.3.0", "TelesecOtherName");
    oidToNameMapping.put("0.2.262.1.10.3.1", "Directory");
    oidToNameMapping.put("0.2.262.1.10.3.2", "DirectoryType");
    oidToNameMapping.put("0.2.262.1.10.3.3", "DirectoryGroup");
    oidToNameMapping.put("0.2.262.1.10.3.4", "DirectoryUser");
    oidToNameMapping.put("0.2.262.1.10.3.5", "SymmetricKeyEntry");
    oidToNameMapping.put("0.2.262.1.10.4", "Package");
    oidToNameMapping.put("0.2.262.1.10.5", "Parameter");
    oidToNameMapping.put("0.2.262.1.10.6", "NameBinding");
    oidToNameMapping.put("0.2.262.1.10.7", "Attribute");
    oidToNameMapping.put("0.2.262.1.10.7.0", "ApplicationGroupIdentifier");
    oidToNameMapping.put("0.2.262.1.10.7.1", "CertificateType");
    oidToNameMapping.put("0.2.262.1.10.7.10", "Subject");
    oidToNameMapping.put("0.2.262.1.10.7.11", "TimeOfRevocation");
    oidToNameMapping.put("0.2.262.1.10.7.12", "UserGroupReference");
    oidToNameMapping.put("0.2.262.1.10.7.13", "Validity");
    oidToNameMapping.put("0.2.262.1.10.7.14", "Zert93");
    oidToNameMapping.put("0.2.262.1.10.7.15", "SecurityMessEnv");
    oidToNameMapping.put("0.2.262.1.10.7.16", "AnonymizedPublicKeyDirectory");
    oidToNameMapping.put("0.2.262.1.10.7.17", "TelesecGivenName");
    oidToNameMapping.put("0.2.262.1.10.7.18", "NameAdditions");
    oidToNameMapping.put("0.2.262.1.10.7.19", "TelesecPostalCode");
    oidToNameMapping.put("0.2.262.1.10.7.2", "TelesecCertificate");
    oidToNameMapping.put("0.2.262.1.10.7.20", "NameDistinguisher");
    oidToNameMapping.put("0.2.262.1.10.7.21", "TelesecCertificateList");
    oidToNameMapping.put("0.2.262.1.10.7.22", "TeletrustCertificateList");
    oidToNameMapping.put("0.2.262.1.10.7.23", "X509CertificateList");
    oidToNameMapping.put("0.2.262.1.10.7.24", "TimeOfIssue");
    oidToNameMapping.put("0.2.262.1.10.7.25", "PhysicalCardNumber");
    oidToNameMapping.put("0.2.262.1.10.7.26", "FileType");
    oidToNameMapping.put("0.2.262.1.10.7.27", "CtlFileIsArchive");
    oidToNameMapping.put("0.2.262.1.10.7.28", "EmailAddress");
    oidToNameMapping.put("0.2.262.1.10.7.29", "CertificateTemplateList");
    oidToNameMapping.put("0.2.262.1.10.7.3", "CertificateNumber");
    oidToNameMapping.put("0.2.262.1.10.7.30", "DirectoryName");
    oidToNameMapping.put("0.2.262.1.10.7.31", "DirectoryTypeName");
    oidToNameMapping.put("0.2.262.1.10.7.32", "DirectoryGroupName");
    oidToNameMapping.put("0.2.262.1.10.7.33", "DirectoryUserName");
    oidToNameMapping.put("0.2.262.1.10.7.34", "RevocationFlag");
    oidToNameMapping.put("0.2.262.1.10.7.35", "SymmetricKeyEntryName");
    oidToNameMapping.put("0.2.262.1.10.7.36", "GlNumber");
    oidToNameMapping.put("0.2.262.1.10.7.37", "GoNumber");
    oidToNameMapping.put("0.2.262.1.10.7.38", "GKeyData");
    oidToNameMapping.put("0.2.262.1.10.7.39", "ZKeyData");
    oidToNameMapping.put("0.2.262.1.10.7.4", "CertificateRevocationList");
    oidToNameMapping.put("0.2.262.1.10.7.40", "KtKeyData");
    oidToNameMapping.put("0.2.262.1.10.7.41", "KtKeyNumber");
    oidToNameMapping.put("0.2.262.1.10.7.5", "CreationDate");
    oidToNameMapping.put("0.2.262.1.10.7.51", "TimeOfRevocationGen");
    oidToNameMapping.put("0.2.262.1.10.7.52", "LiabilityText");
    oidToNameMapping.put("0.2.262.1.10.7.6", "Issuer");
    oidToNameMapping.put("0.2.262.1.10.7.7", "NamingAuthority");
    oidToNameMapping.put("0.2.262.1.10.7.8", "PublicKeyDirectory");
    oidToNameMapping.put("0.2.262.1.10.7.9", "SecurityDomain");
    oidToNameMapping.put("0.2.262.1.10.8", "AttributeGroup");
    oidToNameMapping.put("0.2.262.1.10.9", "Action");
    oidToNameMapping.put("0.4.0.127.0.7", "Bsi");
    oidToNameMapping.put("0.4.0.127.0.7.1", "BsiEcc");
    oidToNameMapping.put("0.4.0.127.0.7.1.1", "BsifieldType");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.1", "BsiPrimeField");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.2", "BsiCharacteristicTwoField");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.2.3", "BsiCharacteristicTwoBasis");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.2.3.1", "BsiGnBasis");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.2.3.2", "BsiTpBasis");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.2.3.3", "BsiPpBasis");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1", "BsiEcdsaSignatures");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1.1", "BsiEcdsaWithSHA1");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1.2", "BsiEcdsaWithSHA224");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1.3", "BsiEcdsaWithSHA256");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1.4", "BsiEcdsaWithSHA384");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1.5", "BsiEcdsaWithSHA512");
    oidToNameMapping.put("0.4.0.127.0.7.1.1.4.1.6", "BsiEcdsaWithRIPEMD160");
    oidToNameMapping.put("0.4.0.127.0.7.1.2", "BsiEcKeyType");
    oidToNameMapping.put("0.4.0.127.0.7.1.2.1", "BsiEcPublicKey");
    oidToNameMapping.put("0.4.0.127.0.7.1.5.1", "BsiKaeg");
    oidToNameMapping.put("0.4.0.127.0.7.1.5.1.1", "BsiKaegWithX963KDF");
    oidToNameMapping.put("0.4.0.127.0.7.1.5.1.2", "BsiKaegWith3DESKDF");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.1", "BsiPK");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.1.1", "BsiPK_DH");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.1.2", "BsiPK_ECDH");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2", "BsiTA");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1", "BsiTA_RSA");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1.1", "BsiTA_RSAv1_5_SHA1");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1.2", "BsiTA_RSAv1_5_SHA256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1.3", "BsiTA_RSAPSS_SHA1");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1.4", "BsiTA_RSAPSS_SHA256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1.5", "BsiTA_RSAv1_5_SHA512");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.1.6", "BsiTA_RSAPSS_SHA512");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.2", "BsiTA_ECDSA");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.2.1", "BsiTA_ECDSA_SHA1");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.2.2", "BsiTA_ECDSA_SHA224");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.2.3", "BsiTA_ECDSA_SHA256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.2.4", "BsiTA_ECDSA_SHA384");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.2.2.5", "BsiTA_ECDSA_SHA512");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3", "BsiCA");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.1", "BsiCA_DH");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.1.1", "BsiCA_DH_3DES_CBC_CBC");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.1.2",
                         "BsiCA_DH_AES_CBC_CMAC_128");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.1.3",
                         "BsiCA_DH_AES_CBC_CMAC_192");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.1.4",
                         "BsiCA_DH_AES_CBC_CMAC_256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.2", "BsiCA_ECDH");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.2.1", "BsiCA_ECDH_3DES_CBC_CBC");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.2.2",
                         "BsiCA_ECDH_AES_CBC_CMAC_128");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.2.3",
                         "BsiCA_ECDH_AES_CBC_CMAC_192");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.3.2.4",
                         "BsiCA_ECDH_AES_CBC_CMAC_256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4", "BsiPACE");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.1", "BsiPACE_DH_GM");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.1.1",
                         "BsiPACE_DH_GM_3DES_CBC_CBC");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.1.2",
                         "BsiPACE_DH_GM_AES_CBC_CMAC_128");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.1.3",
                         "BsiPACE_DH_GM_AES_CBC_CMAC_192");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.1.4",
                         "BsiPACE_DH_GM_AES_CBC_CMAC_256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.2", "BsiPACE_ECDH_GM");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.2.1",
                         "BsiPACE_ECDH_GM_3DES_CBC_CBC");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.2.2",
                         "BsiPACE_ECDH_GM_AES_CBC_CMAC_128");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.2.3",
                         "BsiPACE_ECDH_GM_AES_CBC_CMAC_192");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.2.4",
                         "BsiPACE_ECDH_GM_AES_CBC_CMAC_256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.3", "BsiPACE_DH_IM");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.3.1",
                         "BsiPACE_DH_IM_3DES_CBC_CBC");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.3.2",
                         "BsiPACE_DH_IM_AES_CBC_CMAC_128");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.3.3",
                         "BsiPACE_DH_IM_AES_CBC_CMAC_192");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.3.4",
                         "BsiPACE_DH_IM_AES_CBC_CMAC_256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.4", "BsiPACE_ECDH_IM");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.4.1",
                         "BsiPACE_ECDH_IM_3DES_CBC_CBC");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.4.2",
                         "BsiPACE_ECDH_IM_AES_CBC_CMAC_128");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.4.3",
                         "BsiPACE_ECDH_IM_AES_CBC_CMAC_192");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.4.4.4",
                         "BsiPACE_ECDH_IM_AES_CBC_CMAC_256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5", "BsiRI");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.1", "BsiRI_DH");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.1.1", "BsiRI_DH_SHA1");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.1.2", "BsiRI_DH_SHA224");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.1.3", "BsiRI_DH_SHA256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.1.4", "BsiRI_DH_SHA384");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.1.5", "BsiRI_DH_SHA512");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.2", "BsiRI_ECDH");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.2.1", "BsiRI_ECDH_SHA1");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.2.2", "BsiRI_ECDH_SHA224");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.2.3", "BsiRI_ECDH_SHA256");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.2.4", "BsiRI_ECDH_SHA384");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.5.2.5", "BsiRI_ECDH_SHA512");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.6", "BsiCardInfo");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.7", "BsiEidSecurity");
    oidToNameMapping.put("0.4.0.127.0.7.2.2.8", "BsiPT");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.2", "BsiEACRoles");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.2.1", "BsiEACRolesIS");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.2.2", "BsiEACRolesAT");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.2.3", "BsiEACRolesST");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.3", "BsiTAv2ce");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.3.1", "BsiTAv2ceDescription");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.3.1.1",
                         "BsiTAv2ceDescriptionPlainText");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.3.1.2",
                         "BsiTAv2ceDescriptionIA5String");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.3.1.3",
                         "BsiTAv2ceDescriptionOctetString");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.3.2", "BsiTAv2ceTerminalSector");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.4", "BsiAuxData");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.4.1", "BsiAuxDataBirthday");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.4.2", "BsiAuxDataExpireDate");
    oidToNameMapping.put("0.4.0.127.0.7.3.1.4.3", "BsiAuxDataCommunityID");
    oidToNameMapping.put("0.4.0.127.0.7.3.2.1", "BsiSecObj");
    oidToNameMapping.put("0.4.0.1862", "EtsiQcsProfile");
    oidToNameMapping.put("0.4.0.1862.1", "EtsiQcs");
    oidToNameMapping.put("0.4.0.1862.1.1", "EtsiQcsCompliance");
    oidToNameMapping.put("0.4.0.1862.1.2", "EtsiQcsLimitValue");
    oidToNameMapping.put("0.4.0.1862.1.3", "EtsiQcsRetentionPeriod");
    oidToNameMapping.put("0.4.0.1862.1.4", "EtsiQcsQcSSCD");
    oidToNameMapping.put("0.4.0.1862.1.5", "EtsiQcsQcPDS");
    oidToNameMapping.put("0.4.0.1862.1.6", "EtsiQcsQcType");
    oidToNameMapping.put("0.4.0.1862.1.6.1", "EtsiQctEsign");
    oidToNameMapping.put("0.4.0.1862.1.6.2", "EtsiQctEseal");
    oidToNameMapping.put("0.4.0.1862.1.6.3", "EtsiQctWeb");
    oidToNameMapping.put("0.9.2342.19200300.100.1.1", "UserID");
    oidToNameMapping.put("0.9.2342.19200300.100.1.25", "DomainComponent");
    oidToNameMapping.put("0.9.2342.19200300.100.1.3", "Rfc822Mailbox");
    oidToNameMapping.put("1.0.10118.3.0.49", "Ripemd160");
    oidToNameMapping.put("1.0.10118.3.0.50", "Ripemd128");
    oidToNameMapping.put("1.0.10118.3.0.55", "Whirlpool");
    oidToNameMapping.put("1.2.36.1.3.1.1.1", "Qgpki");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1", "QgpkiPolicies");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.1", "QgpkiMedIntermedCA");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.1.1",
                         "QgpkiMedIntermedIndividual");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.1.2",
                         "QgpkiMedIntermedDeviceControl");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.1.3", "QgpkiMedIntermedDevice");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.1.4",
                         "QgpkiMedIntermedAuthorisedParty");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.1.5",
                         "QgpkiMedIntermedDeviceSystem");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2", "QgpkiMedIssuingCA");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.1", "QgpkiMedIssuingIndividual");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.2",
                         "QgpkiMedIssuingDeviceControl");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.3", "QgpkiMedIssuingDevice");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.4",
                         "QgpkiMedIssuingAuthorisedParty");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.5", "QgpkiMedIssuingClientAuth");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.6", "QgpkiMedIssuingServerAuth");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.7", "QgpkiMedIssuingDataProt");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.2.8", "QgpkiMedIssuingTokenAuth");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.3", "QgpkiBasicIntermedCA");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.3.1",
                         "QgpkiBasicIntermedDeviceSystem");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.4", "QgpkiBasicIssuingCA");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.4.1",
                         "QgpkiBasicIssuingClientAuth");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.4.2",
                         "QgpkiBasicIssuingServerAuth");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.1.4.3",
                         "QgpkiBasicIssuingDataSigning");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.2", "QgpkiAssuranceLevel");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.2.1", "QgpkiAssuranceRudimentary");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.2.2", "QgpkiAssuranceBasic");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.2.3", "QgpkiAssuranceMedium");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.2.4", "QgpkiAssuranceHigh");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.3", "QgpkiCertFunction");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.3.1", "QgpkiFunctionIndividual");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.3.2", "QgpkiFunctionDevice");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.3.3",
                         "QgpkiFunctionAuthorisedParty");
    oidToNameMapping.put("1.2.36.1.3.1.1.1.3.4", "QgpkiFunctionDeviceControl");
    oidToNameMapping.put("1.2.36.1.3.1.2", "Qpspki");
    oidToNameMapping.put("1.2.36.1.3.1.2.1", "QpspkiPolicies");
    oidToNameMapping.put("1.2.36.1.3.1.2.1.2", "QpspkiPolicyBasic");
    oidToNameMapping.put("1.2.36.1.3.1.2.1.3", "QpspkiPolicyMedium");
    oidToNameMapping.put("1.2.36.1.3.1.2.1.4", "QpspkiPolicyHigh");
    oidToNameMapping.put("1.2.36.1.3.1.3.2", "Qtmrpki");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.1", "QtmrpkiPolicies");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.2", "QtmrpkiPurpose");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.2.1", "QtmrpkiIndividual");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.2.2", "QtmrpkiDeviceControl");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.2.3", "QtmrpkiDevice");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.2.4", "QtmrpkiAuthorisedParty");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.2.5", "QtmrpkiDeviceSystem");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.3", "QtmrpkiDevice");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.3.1", "QtmrpkiDriverLicense");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.3.2", "QtmrpkiIndustryAuthority");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.3.3", "QtmrpkiMarineLicense");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.3.4", "QtmrpkiAdultProofOfAge");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.3.5", "QtmrpkiSam");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.4", "QtmrpkiAuthorisedParty");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.4.1", "QtmrpkiTransportInspector");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.4.2", "QtmrpkiPoliceOfficer");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.4.3", "QtmrpkiSystem");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.4.4",
                         "QtmrpkiLiquorLicensingInspector");
    oidToNameMapping.put("1.2.36.1.3.1.3.2.4.5",
                         "QtmrpkiMarineEnforcementOfficer");
    oidToNameMapping.put("1.2.36.1.333.1", "AustralianBusinessNumber");
    oidToNameMapping.put("1.2.36.68980861.1.1.10", "SignetPilot");
    oidToNameMapping.put("1.2.36.68980861.1.1.11", "SignetIntraNet");
    oidToNameMapping.put("1.2.36.68980861.1.1.2", "SignetPersonal");
    oidToNameMapping.put("1.2.36.68980861.1.1.20", "SignetPolicy");
    oidToNameMapping.put("1.2.36.68980861.1.1.3", "SignetBusiness");
    oidToNameMapping.put("1.2.36.68980861.1.1.4", "SignetLegal");
    oidToNameMapping.put("1.2.36.75878867.1.100.1.1",
                         "CertificatesAustraliaPolicy");
    oidToNameMapping.put("1.2.392.200011.61.1.1.1",
                         "MitsubishiSecurityAlgorithm");
    oidToNameMapping.put("1.2.392.200011.61.1.1.1.1", "Misty1-cbc");
    oidToNameMapping.put("1.2.392.200091.100.721.1",
                         "Security Communication (SECOM) EV policy");
    oidToNameMapping.put("1.2.40.0.17.1.22", "A-Trust EV policy");
    oidToNameMapping.put("1.2.410.200004.1", "KisaAlgorithm");
    oidToNameMapping.put("1.2.410.200004.1.1", "Kcdsa");
    oidToNameMapping.put("1.2.410.200004.1.10", "PbeWithHAS160AndSEED-ECB");
    oidToNameMapping.put("1.2.410.200004.1.11", "PbeWithHAS160AndSEED-CBC");
    oidToNameMapping.put("1.2.410.200004.1.12", "PbeWithHAS160AndSEED-CFB");
    oidToNameMapping.put("1.2.410.200004.1.13", "PbeWithHAS160AndSEED-OFB");
    oidToNameMapping.put("1.2.410.200004.1.14", "PbeWithSHA1AndSEED-ECB");
    oidToNameMapping.put("1.2.410.200004.1.15", "PbeWithSHA1AndSEED-CBC");
    oidToNameMapping.put("1.2.410.200004.1.16", "PbeWithSHA1AndSEED-CFB");
    oidToNameMapping.put("1.2.410.200004.1.17", "PbeWithSHA1AndSEED-OFB");
    oidToNameMapping.put("1.2.410.200004.1.2", "Has160");
    oidToNameMapping.put("1.2.410.200004.1.20", "RsaWithHAS160");
    oidToNameMapping.put("1.2.410.200004.1.21", "Kcdsa1");
    oidToNameMapping.put("1.2.410.200004.1.3", "SeedECB");
    oidToNameMapping.put("1.2.410.200004.1.4", "SeedCBC");
    oidToNameMapping.put("1.2.410.200004.1.5", "SeedOFB");
    oidToNameMapping.put("1.2.410.200004.1.6", "SeedCFB");
    oidToNameMapping.put("1.2.410.200004.1.7", "SeedMAC");
    oidToNameMapping.put("1.2.410.200004.1.8", "KcdsaWithHAS160");
    oidToNameMapping.put("1.2.410.200004.1.9", "KcdsaWithSHA1");
    oidToNameMapping.put("1.2.410.200004.10", "Npki");
    oidToNameMapping.put("1.2.410.200004.10.1", "NpkiAttribute");
    oidToNameMapping.put("1.2.410.200004.10.1.1", "NpkiIdentifyData");
    oidToNameMapping.put("1.2.410.200004.10.1.1.1", "NpkiVID");
    oidToNameMapping.put("1.2.410.200004.10.1.1.2", "NpkiEncryptedVID");
    oidToNameMapping.put("1.2.410.200004.10.1.1.3", "NpkiRandomNum");
    oidToNameMapping.put("1.2.410.200004.10.1.1.4", "NpkiVID");
    oidToNameMapping.put("1.2.410.200004.2", "NpkiCP");
    oidToNameMapping.put("1.2.410.200004.2.1", "NpkiSignaturePolicy");
    oidToNameMapping.put("1.2.410.200004.3", "NpkiKP");
    oidToNameMapping.put("1.2.410.200004.4", "NpkiAT");
    oidToNameMapping.put("1.2.410.200004.5", "NpkiLCA");
    oidToNameMapping.put("1.2.410.200004.5.1", "NpkiSignKorea");
    oidToNameMapping.put("1.2.410.200004.5.2", "NpkiSignGate");
    oidToNameMapping.put("1.2.410.200004.5.3", "NpkiNcaSign");
    oidToNameMapping.put("1.2.410.200004.6", "NpkiON");
    oidToNameMapping.put("1.2.410.200004.7", "NpkiAPP");
    oidToNameMapping.put("1.2.410.200004.7.1", "NpkiSMIME");
    oidToNameMapping.put("1.2.410.200004.7.1.1", "NpkiSMIMEAlgo");
    oidToNameMapping.put("1.2.410.200004.7.1.1.1", "NpkiCmsSEEDWrap");
    oidToNameMapping.put("1.2.410.200046.1.1", "Aria1AlgorithmModes");
    oidToNameMapping.put("1.2.410.200046.1.1.1", "Aria128-ecb");
    oidToNameMapping.put("1.2.410.200046.1.1.10", "Aria192-ctr");
    oidToNameMapping.put("1.2.410.200046.1.1.11", "Aria256-ecb");
    oidToNameMapping.put("1.2.410.200046.1.1.12", "Aria256-cbc");
    oidToNameMapping.put("1.2.410.200046.1.1.13", "Aria256-cfb");
    oidToNameMapping.put("1.2.410.200046.1.1.14", "Aria256-ofb");
    oidToNameMapping.put("1.2.410.200046.1.1.15", "Aria256-ctr");
    oidToNameMapping.put("1.2.410.200046.1.1.2", "Aria128-cbc");
    oidToNameMapping.put("1.2.410.200046.1.1.21", "Aria128-cmac");
    oidToNameMapping.put("1.2.410.200046.1.1.22", "Aria192-cmac");
    oidToNameMapping.put("1.2.410.200046.1.1.23", "Aria256-cmac");
    oidToNameMapping.put("1.2.410.200046.1.1.3", "Aria128-cfb");
    oidToNameMapping.put("1.2.410.200046.1.1.31", "Aria128-ocb2");
    oidToNameMapping.put("1.2.410.200046.1.1.32", "Aria192-ocb2");
    oidToNameMapping.put("1.2.410.200046.1.1.33", "Aria256-ocb2");
    oidToNameMapping.put("1.2.410.200046.1.1.34", "Aria128-gcm");
    oidToNameMapping.put("1.2.410.200046.1.1.35", "Aria192-gcm");
    oidToNameMapping.put("1.2.410.200046.1.1.36", "Aria256-gcm");
    oidToNameMapping.put("1.2.410.200046.1.1.37", "Aria128-ccm");
    oidToNameMapping.put("1.2.410.200046.1.1.38", "Aria192-ccm");
    oidToNameMapping.put("1.2.410.200046.1.1.39", "Aria256-ccm");
    oidToNameMapping.put("1.2.410.200046.1.1.4", "Aria128-ofb");
    oidToNameMapping.put("1.2.410.200046.1.1.40", "Aria128-keywrap");
    oidToNameMapping.put("1.2.410.200046.1.1.41", "Aria192-keywrap");
    oidToNameMapping.put("1.2.410.200046.1.1.42", "Aria256-keywrap");
    oidToNameMapping.put("1.2.410.200046.1.1.43", "Aria128-keywrapWithPad");
    oidToNameMapping.put("1.2.410.200046.1.1.44", "Aria192-keywrapWithPad");
    oidToNameMapping.put("1.2.410.200046.1.1.45", "Aria256-keywrapWithPad");
    oidToNameMapping.put("1.2.410.200046.1.1.5", "Aria128-ctr");
    oidToNameMapping.put("1.2.410.200046.1.1.6", "Aria192-ecb");
    oidToNameMapping.put("1.2.410.200046.1.1.7", "Aria192-cbc");
    oidToNameMapping.put("1.2.410.200046.1.1.8", "Aria192-cfb");
    oidToNameMapping.put("1.2.410.200046.1.1.9", "Aria192-ofb");
    oidToNameMapping.put("1.2.643.2.2.10", "HmacGost");
    oidToNameMapping.put("1.2.643.2.2.13.0", "GostWrap");
    oidToNameMapping.put("1.2.643.2.2.13.1", "CryptoProWrap");
    oidToNameMapping.put("1.2.643.2.2.14.0", "NullMeshing");
    oidToNameMapping.put("1.2.643.2.2.14.1", "CryptoProMeshing");
    oidToNameMapping.put("1.2.643.2.2.19", "GostPublicKey");
    oidToNameMapping.put("1.2.643.2.2.20", "Gost94PublicKey");
    oidToNameMapping.put("1.2.643.2.2.21", "GostCipher");
    oidToNameMapping.put("1.2.643.2.2.3", "GostSignature");
    oidToNameMapping.put("1.2.643.2.2.30.0", "TestDigestParams");
    oidToNameMapping.put("1.2.643.2.2.30.1", "CryptoProDigestA");
    oidToNameMapping.put("1.2.643.2.2.31.0", "TestCipherParams");
    oidToNameMapping.put("1.2.643.2.2.31.1", "CryptoProCipherA");
    oidToNameMapping.put("1.2.643.2.2.31.2", "CryptoProCipherB");
    oidToNameMapping.put("1.2.643.2.2.31.3", "CryptoProCipherC");
    oidToNameMapping.put("1.2.643.2.2.31.4", "CryptoProCipherD");
    oidToNameMapping.put("1.2.643.2.2.31.5", "Oscar11Cipher");
    oidToNameMapping.put("1.2.643.2.2.31.6", "Oscar10Cipher");
    oidToNameMapping.put("1.2.643.2.2.31.7", "Ric1Cipher");
    oidToNameMapping.put("1.2.643.2.2.35.0", "TestSignParams");
    oidToNameMapping.put("1.2.643.2.2.35.1", "CryptoProSignA");
    oidToNameMapping.put("1.2.643.2.2.35.2", "CryptoProSignB");
    oidToNameMapping.put("1.2.643.2.2.35.3", "CryptoProSignC");
    oidToNameMapping.put("1.2.643.2.2.36.0", "CryptoProSignXA");
    oidToNameMapping.put("1.2.643.2.2.36.1", "CryptoProSignXB");
    oidToNameMapping.put("1.2.643.2.2.4", "Gost94Signature");
    oidToNameMapping.put("1.2.643.2.2.9", "GostDigest");
    oidToNameMapping.put("1.2.643.2.2.96", "CryptoProECDHWrap");
    oidToNameMapping.put("1.2.752.34.1", "Seis-cp");
    oidToNameMapping.put("1.2.752.34.1.1",
                         "SEIS high-assurance policyIdentifier");
    oidToNameMapping.put("1.2.752.34.1.2", "SEIS GAK policyIdentifier");
    oidToNameMapping.put("1.2.752.34.2", "SEIS pe");
    oidToNameMapping.put("1.2.752.34.3", "SEIS at");
    oidToNameMapping.put("1.2.752.34.3.1", "SEIS at-personalIdentifier");
    oidToNameMapping.put("1.2.840.10040.1", "Module");
    oidToNameMapping.put("1.2.840.10040.1.1", "X9f1-cert-mgmt");
    oidToNameMapping.put("1.2.840.10040.2", "Holdinstruction");
    oidToNameMapping.put("1.2.840.10040.2.1", "Holdinstruction-none");
    oidToNameMapping.put("1.2.840.10040.2.2", "Callissuer");
    oidToNameMapping.put("1.2.840.10040.2.3", "Reject");
    oidToNameMapping.put("1.2.840.10040.2.4", "PickupToken");
    oidToNameMapping.put("1.2.840.10040.3", "Attribute");
    oidToNameMapping.put("1.2.840.10040.3.1", "Countersignature");
    oidToNameMapping.put("1.2.840.10040.3.2", "Attribute-cert");
    oidToNameMapping.put("1.2.840.10040.4", "Algorithm");
    oidToNameMapping.put("1.2.840.10040.4.1", "Dsa");
    oidToNameMapping.put("1.2.840.10040.4.2", "Dsa-match");
    oidToNameMapping.put("1.2.840.10040.4.3", "DsaWithSha1");
    oidToNameMapping.put("1.2.840.10045.1", "FieldType");
    oidToNameMapping.put("1.2.840.10045.1.1", "Prime-field");
    oidToNameMapping.put("1.2.840.10045.1.2", "Characteristic-two-field");
    oidToNameMapping.put("1.2.840.10045.1.2.3", "Characteristic-two-basis");
    oidToNameMapping.put("1.2.840.10045.1.2.3.1", "OnBasis");
    oidToNameMapping.put("1.2.840.10045.1.2.3.2", "TpBasis");
    oidToNameMapping.put("1.2.840.10045.1.2.3.3", "PpBasis");
    oidToNameMapping.put("1.2.840.10045.2", "PublicKeyType");
    oidToNameMapping.put("1.2.840.10045.2.1", "EcPublicKey");
    oidToNameMapping.put("1.2.840.10045.3.0.1", "C2pnb163v1");
    oidToNameMapping.put("1.2.840.10045.3.0.10", "C2pnb208w1");
    oidToNameMapping.put("1.2.840.10045.3.0.11", "C2tnb239v1");
    oidToNameMapping.put("1.2.840.10045.3.0.12", "C2tnb239v2");
    oidToNameMapping.put("1.2.840.10045.3.0.13", "C2tnb239v3");
    oidToNameMapping.put("1.2.840.10045.3.0.16", "C2pnb272w1");
    oidToNameMapping.put("1.2.840.10045.3.0.17", "C2pnb304w1");
    oidToNameMapping.put("1.2.840.10045.3.0.18", "C2tnb359v1");
    oidToNameMapping.put("1.2.840.10045.3.0.19", "C2pnb368w1");
    oidToNameMapping.put("1.2.840.10045.3.0.2", "C2pnb163v2");
    oidToNameMapping.put("1.2.840.10045.3.0.20", "C2tnb431r1");
    oidToNameMapping.put("1.2.840.10045.3.0.3", "C2pnb163v3");
    oidToNameMapping.put("1.2.840.10045.3.0.4", "C2pnb176w1");
    oidToNameMapping.put("1.2.840.10045.3.0.5", "C2tnb191v1");
    oidToNameMapping.put("1.2.840.10045.3.0.6", "C2tnb191v2");
    oidToNameMapping.put("1.2.840.10045.3.0.7", "C2tnb191v3");
    oidToNameMapping.put("1.2.840.10045.3.1.1", "Prime192v1");
    oidToNameMapping.put("1.2.840.10045.3.1.2", "Prime192v2");
    oidToNameMapping.put("1.2.840.10045.3.1.3", "Prime192v3");
    oidToNameMapping.put("1.2.840.10045.3.1.4", "Prime239v1");
    oidToNameMapping.put("1.2.840.10045.3.1.5", "Prime239v2");
    oidToNameMapping.put("1.2.840.10045.3.1.6", "Prime239v3");
    oidToNameMapping.put("1.2.840.10045.3.1.7", "Prime256v1");
    oidToNameMapping.put("1.2.840.10045.4.1", "EcdsaWithSHA1");
    oidToNameMapping.put("1.2.840.10045.4.2", "EcdsaWithRecommended");
    oidToNameMapping.put("1.2.840.10045.4.3", "EcdsaWithSpecified");
    oidToNameMapping.put("1.2.840.10045.4.3.1", "EcdsaWithSHA224");
    oidToNameMapping.put("1.2.840.10045.4.3.2", "EcdsaWithSHA256");
    oidToNameMapping.put("1.2.840.10045.4.3.3", "EcdsaWithSHA384");
    oidToNameMapping.put("1.2.840.10045.4.3.4", "EcdsaWithSHA512");
    oidToNameMapping.put("1.2.840.10046.1", "FieldType");
    oidToNameMapping.put("1.2.840.10046.1.1", "Gf-prime");
    oidToNameMapping.put("1.2.840.10046.2", "NumberType");
    oidToNameMapping.put("1.2.840.10046.2.1", "DhPublicKey");
    oidToNameMapping.put("1.2.840.10046.3", "Scheme");
    oidToNameMapping.put("1.2.840.10046.3.1", "DhStatic");
    oidToNameMapping.put("1.2.840.10046.3.2", "DhEphem");
    oidToNameMapping.put("1.2.840.10046.3.3", "DhHybrid1");
    oidToNameMapping.put("1.2.840.10046.3.4", "DhHybrid2");
    oidToNameMapping.put("1.2.840.10046.3.5", "Mqv2");
    oidToNameMapping.put("1.2.840.10046.3.6", "Mqv1");
    oidToNameMapping.put("1.2.840.10065.2.2", "?");
    oidToNameMapping.put("1.2.840.10065.2.3", "HealthcareLicense");
    oidToNameMapping.put("1.2.840.10065.2.3.1.1", "License?");
    oidToNameMapping.put("1.2.840.113533.7", "Nsn");
    oidToNameMapping.put("1.2.840.113533.7.65", "Nsn-ce");
    oidToNameMapping.put("1.2.840.113533.7.65.0", "EntrustVersInfo");
    oidToNameMapping.put("1.2.840.113533.7.66", "Nsn-alg");
    oidToNameMapping.put("1.2.840.113533.7.66.10", "Cast5CBC");
    oidToNameMapping.put("1.2.840.113533.7.66.11", "Cast5MAC");
    oidToNameMapping.put("1.2.840.113533.7.66.12", "PbeWithMD5AndCAST5-CBC");
    oidToNameMapping.put("1.2.840.113533.7.66.13", "PasswordBasedMac");
    oidToNameMapping.put("1.2.840.113533.7.66.3", "Cast3CBC");
    oidToNameMapping.put("1.2.840.113533.7.67", "Nsn-oc");
    oidToNameMapping.put("1.2.840.113533.7.67.0", "EntrustUser");
    oidToNameMapping.put("1.2.840.113533.7.68", "Nsn-at");
    oidToNameMapping.put("1.2.840.113533.7.68.0", "EntrustCAInfo");
    oidToNameMapping.put("1.2.840.113533.7.68.10", "AttributeCertificate");
    oidToNameMapping.put("1.2.840.113549.1.1", "Pkcs-1");
    oidToNameMapping.put("1.2.840.113549.1.1.1", "RsaEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.10", "RsaPSS");
    oidToNameMapping.put("1.2.840.113549.1.1.11", "Sha256WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.12", "Sha384WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.13", "Sha512WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.14", "Sha224WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.2", "Md2WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.3", "Md4WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.4", "Md5WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.5", "Sha1WithRSAEncryption");
    oidToNameMapping.put("1.2.840.113549.1.1.6", "RsaOAEPEncryptionSET");
    oidToNameMapping.put("1.2.840.113549.1.1.7", "RsaOAEP");
    oidToNameMapping.put("1.2.840.113549.1.1.8", "Pkcs1-MGF");
    oidToNameMapping.put("1.2.840.113549.1.1.9", "RsaOAEP-pSpecified");
    oidToNameMapping.put("1.2.840.113549.1.12", "Pkcs-12");
    oidToNameMapping.put("1.2.840.113549.1.12.1", "Pkcs-12-PbeIds");
    oidToNameMapping.put("1.2.840.113549.1.12.1.1", "PbeWithSHAAnd128BitRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.1.2", "PbeWithSHAAnd40BitRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.1.3",
                         "PbeWithSHAAnd3-KeyTripleDES-CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.1.4",
                         "PbeWithSHAAnd2-KeyTripleDES-CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.1.5",
                         "PbeWithSHAAnd128BitRC2-CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.1.6",
                         "PbeWithSHAAnd40BitRC2-CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.10", "Pkcs-12Version1");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1", "Pkcs-12BadIds");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1.1", "Pkcs-12-keyBag");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1.2",
                         "Pkcs-12-pkcs-8ShroudedKeyBag");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1.3", "Pkcs-12-certBag");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1.4", "Pkcs-12-crlBag");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1.5", "Pkcs-12-secretBag");
    oidToNameMapping.put("1.2.840.113549.1.12.10.1.6",
                         "Pkcs-12-safeContentsBag");
    oidToNameMapping.put("1.2.840.113549.1.12.2", "Pkcs-12-ESPVKID");
    oidToNameMapping.put("1.2.840.113549.1.12.2.1",
                         "Pkcs-12-PKCS8KeyShrouding");
    oidToNameMapping.put("1.2.840.113549.1.12.3", "Pkcs-12-BagIds");
    oidToNameMapping.put("1.2.840.113549.1.12.3.1", "Pkcs-12-keyBagId");
    oidToNameMapping.put("1.2.840.113549.1.12.3.2", "Pkcs-12-certAndCRLBagId");
    oidToNameMapping.put("1.2.840.113549.1.12.3.3", "Pkcs-12-secretBagId");
    oidToNameMapping.put("1.2.840.113549.1.12.3.4", "Pkcs-12-safeContentsId");
    oidToNameMapping.put("1.2.840.113549.1.12.3.5",
                         "Pkcs-12-pkcs-8ShroudedKeyBagId");
    oidToNameMapping.put("1.2.840.113549.1.12.4", "Pkcs-12-CertBagID");
    oidToNameMapping.put("1.2.840.113549.1.12.4.1", "Pkcs-12-X509CertCRLBagID");
    oidToNameMapping.put("1.2.840.113549.1.12.4.2", "Pkcs-12-SDSICertBagID");
    oidToNameMapping.put("1.2.840.113549.1.12.5", "Pkcs-12-OID");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1", "Pkcs-12-PBEID");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.1",
                         "Pkcs-12-PBEWithSha1And128BitRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.2",
                         "Pkcs-12-PBEWithSha1And40BitRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.3",
                         "Pkcs-12-PBEWithSha1AndTripleDESCBC");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.4",
                         "Pkcs-12-PBEWithSha1And128BitRC2CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.5",
                         "Pkcs-12-PBEWithSha1And40BitRC2CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.6",
                         "Pkcs-12-PBEWithSha1AndRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.5.1.7",
                         "Pkcs-12-PBEWithSha1AndRC2CBC");
    oidToNameMapping.put("1.2.840.113549.1.12.5.2", "Pkcs-12-EnvelopingID");
    oidToNameMapping.put("1.2.840.113549.1.12.5.2.1",
                         "Pkcs-12-RSAEncryptionWith128BitRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.5.2.2",
                         "Pkcs-12-RSAEncryptionWith40BitRC4");
    oidToNameMapping.put("1.2.840.113549.1.12.5.2.3",
                         "Pkcs-12-RSAEncryptionWithTripleDES");
    oidToNameMapping.put("1.2.840.113549.1.12.5.3", "Pkcs-12-SignatureID");
    oidToNameMapping.put("1.2.840.113549.1.12.5.3.1",
                         "Pkcs-12-RSASignatureWithSHA1Digest");
    oidToNameMapping.put("1.2.840.113549.1.15.1", "Pkcs15modules");
    oidToNameMapping.put("1.2.840.113549.1.15.2", "Pkcs15attributes");
    oidToNameMapping.put("1.2.840.113549.1.15.3", "Pkcs15contentType");
    oidToNameMapping.put("1.2.840.113549.1.15.3.1", "Pkcs15content");
    oidToNameMapping.put("1.2.840.113549.1.2", "BsafeRsaEncr");
    oidToNameMapping.put("1.2.840.113549.1.3", "Pkcs-3");
    oidToNameMapping.put("1.2.840.113549.1.3.1", "DhKeyAgreement");
    oidToNameMapping.put("1.2.840.113549.1.5", "Pkcs-5");
    oidToNameMapping.put("1.2.840.113549.1.5.1", "PbeWithMD2AndDES-CBC");
    oidToNameMapping.put("1.2.840.113549.1.5.10", "PbeWithSHAAndDES-CBC");
    oidToNameMapping.put("1.2.840.113549.1.5.12", "Pkcs5PBKDF2");
    oidToNameMapping.put("1.2.840.113549.1.5.13", "Pkcs5PBES2");
    oidToNameMapping.put("1.2.840.113549.1.5.14", "Pkcs5PBMAC1");
    oidToNameMapping.put("1.2.840.113549.1.5.3", "PbeWithMD5AndDES-CBC");
    oidToNameMapping.put("1.2.840.113549.1.5.4", "PbeWithMD2AndRC2-CBC");
    oidToNameMapping.put("1.2.840.113549.1.5.6", "PbeWithMD5AndRC2-CBC");
    oidToNameMapping.put("1.2.840.113549.1.5.9", "PbeWithMD5AndXOR");
    oidToNameMapping.put("1.2.840.113549.1.7", "Pkcs-7");
    oidToNameMapping.put("1.2.840.113549.1.7.1", "Data");
    oidToNameMapping.put("1.2.840.113549.1.7.2", "SignedData");
    oidToNameMapping.put("1.2.840.113549.1.7.3", "EnvelopedData");
    oidToNameMapping.put("1.2.840.113549.1.7.4", "SignedAndEnvelopedData");
    oidToNameMapping.put("1.2.840.113549.1.7.5", "DigestedData");
    oidToNameMapping.put("1.2.840.113549.1.7.6", "EncryptedData");
    oidToNameMapping.put("1.2.840.113549.1.7.7", "DataWithAttributes");
    oidToNameMapping.put("1.2.840.113549.1.7.8", "EncryptedPrivateKeyInfo");
    oidToNameMapping.put("1.2.840.113549.1.9", "Pkcs-9");
    oidToNameMapping.put("1.2.840.113549.1.9.1", "EmailAddress");
    oidToNameMapping.put("1.2.840.113549.1.9.10", "IssuerAndSerialNumber");
    oidToNameMapping.put("1.2.840.113549.1.9.11", "PasswordCheck");
    oidToNameMapping.put("1.2.840.113549.1.9.12", "PublicKey");
    oidToNameMapping.put("1.2.840.113549.1.9.13", "SigningDescription");
    oidToNameMapping.put("1.2.840.113549.1.9.14", "ExtensionRequest");
    oidToNameMapping.put("1.2.840.113549.1.9.15", "SMIMECapabilities");
    oidToNameMapping.put("1.2.840.113549.1.9.15.1", "PreferSignedData");
    oidToNameMapping.put("1.2.840.113549.1.9.15.2", "CanNotDecryptAny");
    oidToNameMapping.put("1.2.840.113549.1.9.15.3", "ReceiptRequest");
    oidToNameMapping.put("1.2.840.113549.1.9.15.4", "Receipt");
    oidToNameMapping.put("1.2.840.113549.1.9.15.5", "ContentHints");
    oidToNameMapping.put("1.2.840.113549.1.9.15.6", "MlExpansionHistory");
    oidToNameMapping.put("1.2.840.113549.1.9.16", "Id-sMIME");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0", "Id-mod");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.1", "Id-mod-cms");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.2", "Id-mod-ess");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.3", "Id-mod-oid");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.4", "Id-mod-msg-v3");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.5",
                         "Id-mod-ets-eSignature-88");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.6",
                         "Id-mod-ets-eSignature-97");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.7",
                         "Id-mod-ets-eSigPolicy-88");
    oidToNameMapping.put("1.2.840.113549.1.9.16.0.8",
                         "Id-mod-ets-eSigPolicy-88");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1", "ContentType");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.1", "Receipt");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.10", "ScvpCertValRequest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.11", "ScvpCertValResponse");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.12", "ScvpValPolRequest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.13", "ScvpValPolResponse");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.14", "AttrCertEncAttrs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.15", "TSReq");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.16", "FirmwarePackage");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.17", "FirmwareLoadReceipt");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.18", "FirmwareLoadError");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.19", "ContentCollection");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.2", "AuthData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.20", "ContentWithAttrs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.21", "EncKeyWithID");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.22", "EncPEPSI");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.23", "AuthEnvelopedData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.24", "RouteOriginAttest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.25", "SymmetricKeyPackage");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.26", "RpkiManifest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.27", "AsciiTextWithCRLF");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.28", "Xml");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.29", "Pdf");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.3", "PublishCert");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.30", "Postscript");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.31", "TimestampedData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.32", "AsAdjacencyAttest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.33", "RpkiTrustAnchor");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.34", "TrustAnchorList");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.4", "TSTInfo");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.5", "TDTInfo");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.6", "ContentInfo");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.7", "DVCSRequestData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.8", "DVCSResponseData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.1.9", "CompressedData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.11", "Capabilities");
    oidToNameMapping.put("1.2.840.113549.1.9.16.11.1", "PreferBinaryInside");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2", "AuthenticatedAttributes");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.1", "ReceiptRequest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.10", "ContentReference");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.11", "EncrypKeyPref");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.12", "SigningCertificate");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.13", "SmimeEncryptCerts");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.14", "TimeStampToken");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.15", "SigPolicyId");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.16", "CommitmentType");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.17", "SignerLocation");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.18", "SignerAttr");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.19", "OtherSigCert");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.2", "SecurityLabel");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.20", "ContentTimestamp");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.21", "CertificateRefs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.22", "RevocationRefs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.23", "CertValues");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.24", "RevocationValues");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.25", "EscTimeStamp");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.26", "CertCRLTimestamp");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.27", "ArchiveTimeStamp");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.28", "SignatureType");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.29", "DvcsDvc");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.3", "MlExpandHistory");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.30", "CekReference");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.31", "MaxCEKDecrypts");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.32", "KekDerivationAlg");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.33", "IntendedRecipients");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.34", "CmcUnsignedData");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.35", "FwPackageID");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.36", "FwTargetHardwareIDs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.37", "FwDecryptKeyID");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.38", "FwImplCryptAlgs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.39", "FwWrappedFirmwareKey");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.4", "ContentHint");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.40",
                         "FwCommunityIdentifiers");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.41", "FwPkgMessageDigest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.42", "FwPackageInfo");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.43", "FwImplCompressAlgs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.44",
                         "EtsAttrCertificateRefs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.45", "EtsAttrRevocationRefs");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.46", "BinarySigningTime");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.47", "SigningCertificateV2");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.48", "EtsArchiveTimeStampV2");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.49", "ErInternal");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.5", "MsgSigDigest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.50", "ErExternal");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.51", "MultipleSignatures");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.6", "EncapContentType");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.7", "ContentIdentifier");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.8", "MacValue");
    oidToNameMapping.put("1.2.840.113549.1.9.16.2.9", "EquivalentLabels");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.1", "EsDHwith3DES");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.10", "SsDH");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.11", "HmacWith3DESwrap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.12", "HmacWithAESwrap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.13", "Md5XorExperiment");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.14", "RsaKEM");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.15", "AuthEnc128");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.16", "AuthEnc256");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.2", "EsDHwithRC2");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.3", "3desWrap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.4", "Rc2Wrap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.5", "EsDH");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.6", "Cms3DESwrap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.7", "CmsRC2wrap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.8", "Zlib");
    oidToNameMapping.put("1.2.840.113549.1.9.16.3.9", "PwriKEK");
    oidToNameMapping.put("1.2.840.113549.1.9.16.4.1", "CertDist-ldap");
    oidToNameMapping.put("1.2.840.113549.1.9.16.5.1",
                         "SigPolicyQualifier-spuri x");
    oidToNameMapping.put("1.2.840.113549.1.9.16.5.2",
                         "SigPolicyQualifier-spUserNotice");
    oidToNameMapping.put("1.2.840.113549.1.9.16.6.1", "ProofOfOrigin");
    oidToNameMapping.put("1.2.840.113549.1.9.16.6.2", "ProofOfReceipt");
    oidToNameMapping.put("1.2.840.113549.1.9.16.6.3", "ProofOfDelivery");
    oidToNameMapping.put("1.2.840.113549.1.9.16.6.4", "ProofOfSender");
    oidToNameMapping.put("1.2.840.113549.1.9.16.6.5", "ProofOfApproval");
    oidToNameMapping.put("1.2.840.113549.1.9.16.6.6", "ProofOfCreation");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.1", "GlUseKEK");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.10", "GlFailInfo");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.11", "GlaQueryRequest");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.12", "GlaQueryResponse");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.13", "GlProvideCert");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.14", "GlUpdateCert");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.15", "GlKey");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.2", "GlDelete");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.3", "GlAddMember");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.4", "GlDeleteMember");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.5", "GlRekey");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.6", "GlAddOwner");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.7", "GlRemoveOwner");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.8", "GlkCompromise");
    oidToNameMapping.put("1.2.840.113549.1.9.16.8.9", "GlkRefresh");
    oidToNameMapping.put("1.2.840.113549.1.9.16.9", "SignatureTypeIdentifier");
    oidToNameMapping.put("1.2.840.113549.1.9.16.9.1", "OriginatorSig");
    oidToNameMapping.put("1.2.840.113549.1.9.16.9.2", "DomainSig");
    oidToNameMapping.put("1.2.840.113549.1.9.16.9.3",
                         "AdditionalAttributesSig");
    oidToNameMapping.put("1.2.840.113549.1.9.16.9.4", "ReviewSig");
    oidToNameMapping.put("1.2.840.113549.1.9.2", "UnstructuredName");
    oidToNameMapping.put("1.2.840.113549.1.9.20", "FriendlyName (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.21", "LocalKeyID (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.22", "CertTypes (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.22.1",
                         "X509Certificate (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.22.2",
                         "SdsiCertificate (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.23", "CrlTypes (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.23.1", "X509Crl (for PKCS ");
    oidToNameMapping.put("1.2.840.113549.1.9.24", "Pkcs9objectClass");
    oidToNameMapping.put("1.2.840.113549.1.9.25", "Pkcs9attributes");
    oidToNameMapping.put("1.2.840.113549.1.9.25.1", "Pkcs15Token");
    oidToNameMapping.put("1.2.840.113549.1.9.25.2", "EncryptedPrivateKeyInfo");
    oidToNameMapping.put("1.2.840.113549.1.9.25.3", "RandomNonce");
    oidToNameMapping.put("1.2.840.113549.1.9.25.4", "SequenceNumber");
    oidToNameMapping.put("1.2.840.113549.1.9.25.5", "Pkcs7PDU");
    oidToNameMapping.put("1.2.840.113549.1.9.26", "Pkcs9syntax");
    oidToNameMapping.put("1.2.840.113549.1.9.27", "Pkcs9matchingRules");
    oidToNameMapping.put("1.2.840.113549.1.9.3", "ContentType");
    oidToNameMapping.put("1.2.840.113549.1.9.4", "MessageDigest");
    oidToNameMapping.put("1.2.840.113549.1.9.5", "SigningTime");
    oidToNameMapping.put("1.2.840.113549.1.9.6", "Countersignature");
    oidToNameMapping.put("1.2.840.113549.1.9.7", "ChallengePassword");
    oidToNameMapping.put("1.2.840.113549.1.9.8", "UnstructuredAddress");
    oidToNameMapping.put("1.2.840.113549.1.9.9",
                         "ExtendedCertificateAttributes");
    oidToNameMapping.put("1.2.840.113549.2", "DigestAlgorithm");
    oidToNameMapping.put("1.2.840.113549.2.10", "HmacWithSHA384");
    oidToNameMapping.put("1.2.840.113549.2.11", "HmacWithSHA512");
    oidToNameMapping.put("1.2.840.113549.2.2", "Md2");
    oidToNameMapping.put("1.2.840.113549.2.4", "Md4");
    oidToNameMapping.put("1.2.840.113549.2.5", "Md5");
    oidToNameMapping.put("1.2.840.113549.2.7", "HmacWithSHA1");
    oidToNameMapping.put("1.2.840.113549.2.8", "HmacWithSHA224");
    oidToNameMapping.put("1.2.840.113549.2.9", "HmacWithSHA256");
    oidToNameMapping.put("1.2.840.113549.3", "EncryptionAlgorithm");
    oidToNameMapping.put("1.2.840.113549.3.10", "DesCDMF");
    oidToNameMapping.put("1.2.840.113549.3.2", "Rc2CBC");
    oidToNameMapping.put("1.2.840.113549.3.3", "Rc2ECB");
    oidToNameMapping.put("1.2.840.113549.3.4", "Rc4");
    oidToNameMapping.put("1.2.840.113549.3.5", "Rc4WithMAC");
    oidToNameMapping.put("1.2.840.113549.3.6", "Desx-CBC");
    oidToNameMapping.put("1.2.840.113549.3.7", "Des-EDE3-CBC");
    oidToNameMapping.put("1.2.840.113549.3.8", "Rc5CBC");
    oidToNameMapping.put("1.2.840.113549.3.9", "Rc5-CBCPad");
    oidToNameMapping.put("1.2.840.113556.1.2.241", "DeliveryMechanism");
    oidToNameMapping.put("1.2.840.113556.1.2.281", "NtSecurityDescriptor");
    oidToNameMapping.put("1.2.840.113556.1.3.0", "Site-Addressing");
    oidToNameMapping.put("1.2.840.113556.1.3.13", "ClassSchema");
    oidToNameMapping.put("1.2.840.113556.1.3.14", "AttributeSchema");
    oidToNameMapping.put("1.2.840.113556.1.3.17", "Mailbox-Agent");
    oidToNameMapping.put("1.2.840.113556.1.3.22", "Mailbox");
    oidToNameMapping.put("1.2.840.113556.1.3.23", "Container");
    oidToNameMapping.put("1.2.840.113556.1.3.46", "MailRecipient");
    oidToNameMapping.put("1.2.840.113556.1.4.1327", "PKIDefaultKeySpec");
    oidToNameMapping.put("1.2.840.113556.1.4.1328", "PKIKeyUsage");
    oidToNameMapping.put("1.2.840.113556.1.4.1329", "PKIMaxIssuingDepth");
    oidToNameMapping.put("1.2.840.113556.1.4.1330", "PKICriticalExtensions");
    oidToNameMapping.put("1.2.840.113556.1.4.1331", "PKIExpirationPeriod");
    oidToNameMapping.put("1.2.840.113556.1.4.1332", "PKIOverlapPeriod");
    oidToNameMapping.put("1.2.840.113556.1.4.1333", "PKIExtendedKeyUsage");
    oidToNameMapping.put("1.2.840.113556.1.4.1334", "PKIDefaultCSPs");
    oidToNameMapping.put("1.2.840.113556.1.4.1335", "PKIEnrollmentAccess");
    oidToNameMapping.put("1.2.840.113556.1.4.1429", "MsPKI-RA-Signature");
    oidToNameMapping.put("1.2.840.113556.1.4.1430", "MsPKI-Enrollment-Flag");
    oidToNameMapping.put("1.2.840.113556.1.4.1431", "MsPKI-Private-Key-Flag");
    oidToNameMapping.put("1.2.840.113556.1.4.1432",
                         "MsPKI-Certificate-Name-Flag");
    oidToNameMapping.put("1.2.840.113556.1.4.1433", "MsPKI-Minimal-Key-Size");
    oidToNameMapping.put("1.2.840.113556.1.4.1434",
                         "MsPKI-Template-Schema-Version");
    oidToNameMapping.put("1.2.840.113556.1.4.1435",
                         "MsPKI-Template-Minor-Revision");
    oidToNameMapping.put("1.2.840.113556.1.4.1436", "MsPKI-Cert-Template-OID");
    oidToNameMapping.put("1.2.840.113556.1.4.1437",
                         "MsPKI-Supersede-Templates");
    oidToNameMapping.put("1.2.840.113556.1.4.1438", "MsPKI-RA-Policies");
    oidToNameMapping.put("1.2.840.113556.1.4.1439", "MsPKI-Certificate-Policy");
    oidToNameMapping.put("1.2.840.113556.1.4.145", "Revision");
    oidToNameMapping.put("1.2.840.113556.1.4.1674",
                         "MsPKI-Certificate-Application-Policy");
    oidToNameMapping.put("1.2.840.113556.1.4.1675",
                         "MsPKI-RA-Application-Policies");
    oidToNameMapping.put("1.2.840.113556.4.3", "MicrosoftExcel");
    oidToNameMapping.put("1.2.840.113556.4.4", "TitledWithOID");
    oidToNameMapping.put("1.2.840.113556.4.5", "MicrosoftPowerPoint");
    oidToNameMapping.put("1.2.840.113583.1", "AdobeAcrobat");
    oidToNameMapping.put("1.2.840.113583.1.1", "AcrobatSecurity");
    oidToNameMapping.put("1.2.840.113583.1.1.1", "PdfPassword");
    oidToNameMapping.put("1.2.840.113583.1.1.10", "PdfPPLKLiteCredential");
    oidToNameMapping.put("1.2.840.113583.1.1.2", "PdfDefaultSigningCredential");
    oidToNameMapping.put("1.2.840.113583.1.1.3",
                         "PdfDefaultEncryptionCredential");
    oidToNameMapping.put("1.2.840.113583.1.1.4", "PdfPasswordTimeout");
    oidToNameMapping.put("1.2.840.113583.1.1.5", "PdfAuthenticDocumentsTrust");
    oidToNameMapping.put("1.2.840.113583.1.1.6", "PdfDynamicContentTrust");
    oidToNameMapping.put("1.2.840.113583.1.1.7", "PdfUbiquityTrust");
    oidToNameMapping.put("1.2.840.113583.1.1.8", "PdfRevocationInfoArchival");
    oidToNameMapping.put("1.2.840.113583.1.1.9", "PdfX509Extension");
    oidToNameMapping.put("1.2.840.113583.1.1.9.1", "PdfTimeStamp");
    oidToNameMapping.put("1.2.840.113583.1.1.9.2", "PdfArchiveRevInfo");
    oidToNameMapping.put("1.2.840.113583.1.2 ", "acrobatCPS");
    oidToNameMapping.put("1.2.840.113583.1.2.1", "PdfAuthenticDocumentsCPS");
    oidToNameMapping.put("1.2.840.113583.1.2.2", "PdfTestCPS");
    oidToNameMapping.put("1.2.840.113583.1.2.3", "PdfUbiquityCPS");
    oidToNameMapping.put("1.2.840.113583.1.2.4", "PdfAdhocCPS");
    oidToNameMapping.put("1.2.840.113583.1.7", "AcrobatUbiquity");
    oidToNameMapping.put("1.2.840.113583.1.7.1", "PdfUbiquitySubRights");
    oidToNameMapping.put("1.2.840.113583.1.9", "AcrobatExtension");
    oidToNameMapping.put("1.2.840.113628.114.1.7", "AdobePKCS7");
    oidToNameMapping.put("1.2.840.113635.100", "AppleDataSecurity");
    oidToNameMapping.put("1.2.840.113635.100.1", "AppleTrustPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.1", "AppleISignTP");
    oidToNameMapping.put("1.2.840.113635.100.1.10",
                         "AppleSWUpdateSigningPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.11", "AppleIPSecPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.12", "AppleIChatPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.13", "AppleResourceSignPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.14", "ApplePKINITClientPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.15", "ApplePKINITServerPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.16", "AppleCodeSigningPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.17",
                         "ApplePackageSigningPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.2", "AppleX509Basic");
    oidToNameMapping.put("1.2.840.113635.100.1.3", "AppleSSLPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.4", "AppleLocalCertGenPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.5", "AppleCSRGenPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.6", "AppleCRLPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.7", "AppleOCSPPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.8", "AppleSMIMEPolicy");
    oidToNameMapping.put("1.2.840.113635.100.1.9", "AppleEAPPolicy");
    oidToNameMapping.put("1.2.840.113635.100.2", "AppleSecurityAlgorithm");
    oidToNameMapping.put("1.2.840.113635.100.2.1", "AppleFEE");
    oidToNameMapping.put("1.2.840.113635.100.2.2", "AppleASC");
    oidToNameMapping.put("1.2.840.113635.100.2.3", "AppleFEE_MD5");
    oidToNameMapping.put("1.2.840.113635.100.2.4", "AppleFEE_SHA1");
    oidToNameMapping.put("1.2.840.113635.100.2.5", "AppleFEED");
    oidToNameMapping.put("1.2.840.113635.100.2.6", "AppleFEEDEXP");
    oidToNameMapping.put("1.2.840.113635.100.2.7", "AppleECDSA");
    oidToNameMapping.put("1.2.840.113635.100.3", "AppleDotMacCertificate");
    oidToNameMapping.put("1.2.840.113635.100.3.1",
                         "AppleDotMacCertificateRequest");
    oidToNameMapping.put("1.2.840.113635.100.3.2",
                         "AppleDotMacCertificateExtension");
    oidToNameMapping.put("1.2.840.113635.100.3.3",
                         "AppleDotMacCertificateRequestValues");
    oidToNameMapping.put("1.2.840.113635.100.4", "AppleExtendedKeyUsage");
    oidToNameMapping.put("1.2.840.113635.100.4.1", "AppleCodeSigning");
    oidToNameMapping.put("1.2.840.113635.100.4.1.1",
                         "AppleCodeSigningDevelopment");
    oidToNameMapping.put("1.2.840.113635.100.4.1.2",
                         "AppleSoftwareUpdateSigning");
    oidToNameMapping.put("1.2.840.113635.100.4.1.3",
                         "AppleCodeSigningThirdParty");
    oidToNameMapping.put("1.2.840.113635.100.4.1.4", "AppleResourceSigning");
    oidToNameMapping.put("1.2.840.113635.100.4.2", "AppleIChatSigning");
    oidToNameMapping.put("1.2.840.113635.100.4.3", "AppleIChatEncryption");
    oidToNameMapping.put("1.2.840.113635.100.4.4", "AppleSystemIdentity");
    oidToNameMapping.put("1.2.840.113635.100.4.5", "AppleCryptoEnv");
    oidToNameMapping.put("1.2.840.113635.100.4.5.1",
                         "AppleCryptoProductionEnv");
    oidToNameMapping.put("1.2.840.113635.100.4.5.2",
                         "AppleCryptoMaintenanceEnv");
    oidToNameMapping.put("1.2.840.113635.100.4.5.3", "AppleCryptoTestEnv");
    oidToNameMapping.put("1.2.840.113635.100.4.5.4",
                         "AppleCryptoDevelopmentEnv");
    oidToNameMapping.put("1.2.840.113635.100.4.6", "AppleCryptoQoS");
    oidToNameMapping.put("1.2.840.113635.100.4.6.1", "AppleCryptoTier0QoS");
    oidToNameMapping.put("1.2.840.113635.100.4.6.2", "AppleCryptoTier1QoS");
    oidToNameMapping.put("1.2.840.113635.100.4.6.3", "AppleCryptoTier2QoS");
    oidToNameMapping.put("1.2.840.113635.100.4.6.4", "AppleCryptoTier3QoS");
    oidToNameMapping.put("1.2.840.113635.100.5", "AppleCertificatePolicies");
    oidToNameMapping.put("1.2.840.113635.100.5.1", "AppleCertificatePolicyID");
    oidToNameMapping.put("1.2.840.113635.100.5.2",
                         "AppleDotMacCertificatePolicyID");
    oidToNameMapping.put("1.2.840.113635.100.5.3",
                         "AppleADCCertificatePolicyID");
    oidToNameMapping.put("1.2.840.113635.100.6", "AppleCertificateExtensions");
    oidToNameMapping.put("1.2.840.113635.100.6.1",
                         "AppleCertificateExtensionCodeSigning");
    oidToNameMapping.put("1.2.840.113635.100.6.1.1",
                         "AppleCertificateExtensionAppleSigning");
    oidToNameMapping.put("1.2.840.113635.100.6.1.2",
                         "AppleCertificateExtensionADCDeveloperSigning");
    oidToNameMapping.put("1.2.840.113635.100.6.1.3",
                         "AppleCertificateExtensionADCAppleSigning");
    oidToNameMapping.put("1.2.840.114021.1.6.1",
                         "Identrus unknown policyIdentifier");
    oidToNameMapping.put("1.2.840.114021.4.1", "IdentrusOCSP");
    oidToNameMapping.put("1.3.101.1.4", "Thawte-ce");
    oidToNameMapping.put("1.3.101.1.4.1", "StrongExtranet");
    oidToNameMapping.put("1.3.101.110", "X25519");
    oidToNameMapping.put("1.3.101.111", "X448");
    oidToNameMapping.put("1.3.101.112", "Ed25519");
    oidToNameMapping.put("1.3.101.113", "Ed448");
    oidToNameMapping.put("1.3.12.2.1011.7.1", "DecEncryptionAlgorithm");
    oidToNameMapping.put("1.3.12.2.1011.7.1.2", "DecDEA");
    oidToNameMapping.put("1.3.12.2.1011.7.2", "DecHashAlgorithm");
    oidToNameMapping.put("1.3.12.2.1011.7.2.1", "DecMD2");
    oidToNameMapping.put("1.3.12.2.1011.7.2.2", "DecMD4");
    oidToNameMapping.put("1.3.12.2.1011.7.3", "DecSignatureAlgorithm");
    oidToNameMapping.put("1.3.12.2.1011.7.3.1", "DecMD2withRSA");
    oidToNameMapping.put("1.3.12.2.1011.7.3.2", "DecMD4withRSA");
    oidToNameMapping.put("1.3.12.2.1011.7.3.3", "DecDEAMAC");
    oidToNameMapping.put("1.3.132.0.1", "Sect163k1");
    oidToNameMapping.put("1.3.132.0.10", "Secp256k1");
    oidToNameMapping.put("1.3.132.0.15", "Sect163r2");
    oidToNameMapping.put("1.3.132.0.16", "Sect283k1");
    oidToNameMapping.put("1.3.132.0.17", "Sect283r1");
    oidToNameMapping.put("1.3.132.0.2", "Sect163r1");
    oidToNameMapping.put("1.3.132.0.22", "Sect131r1");
    oidToNameMapping.put("1.3.132.0.23", "Sect131r2");
    oidToNameMapping.put("1.3.132.0.24", "Sect193r1");
    oidToNameMapping.put("1.3.132.0.25", "Sect193r2");
    oidToNameMapping.put("1.3.132.0.26", "Sect233k1");
    oidToNameMapping.put("1.3.132.0.27", "Sect233r1");
    oidToNameMapping.put("1.3.132.0.28", "Secp128r1");
    oidToNameMapping.put("1.3.132.0.29", "Secp128r2");
    oidToNameMapping.put("1.3.132.0.3", "Sect239k1");
    oidToNameMapping.put("1.3.132.0.30", "Secp160r2");
    oidToNameMapping.put("1.3.132.0.31", "Secp192k1");
    oidToNameMapping.put("1.3.132.0.32", "Secp224k1");
    oidToNameMapping.put("1.3.132.0.33", "Secp224r1");
    oidToNameMapping.put("1.3.132.0.34", "Secp384r1");
    oidToNameMapping.put("1.3.132.0.35", "Secp521r1");
    oidToNameMapping.put("1.3.132.0.36", "Sect409k1");
    oidToNameMapping.put("1.3.132.0.37", "Sect409r1");
    oidToNameMapping.put("1.3.132.0.38", "Sect571k1");
    oidToNameMapping.put("1.3.132.0.39", "Sect571r1");
    oidToNameMapping.put("1.3.132.0.4", "Sect113r1");
    oidToNameMapping.put("1.3.132.0.5", "Sect113r2");
    oidToNameMapping.put("1.3.132.0.6", "Secp112r1");
    oidToNameMapping.put("1.3.132.0.7", "Secp112r2");
    oidToNameMapping.put("1.3.132.0.8", "Secp160r1");
    oidToNameMapping.put("1.3.132.0.9", "Secp160k1");
    oidToNameMapping.put("1.3.14.2.26.5", "Sha");
    oidToNameMapping.put("1.3.14.3.2.1.1", "Rsa");
    oidToNameMapping.put("1.3.14.3.2.10", "DesMAC");
    oidToNameMapping.put("1.3.14.3.2.11", "RsaSignature");
    oidToNameMapping.put("1.3.14.3.2.12", "Dsa");
    oidToNameMapping.put("1.3.14.3.2.13", "DsaWithSHA");
    oidToNameMapping.put("1.3.14.3.2.14", "Mdc2WithRSASignature");
    oidToNameMapping.put("1.3.14.3.2.15", "ShaWithRSASignature");
    oidToNameMapping.put("1.3.14.3.2.16", "DhWithCommonModulus");
    oidToNameMapping.put("1.3.14.3.2.17", "DesEDE");
    oidToNameMapping.put("1.3.14.3.2.18", "Sha");
    oidToNameMapping.put("1.3.14.3.2.19", "Mdc-2");
    oidToNameMapping.put("1.3.14.3.2.2", "Md4WitRSA");
    oidToNameMapping.put("1.3.14.3.2.2.1", "Sqmod-N");
    oidToNameMapping.put("1.3.14.3.2.20", "DsaCommon");
    oidToNameMapping.put("1.3.14.3.2.21", "DsaCommonWithSHA");
    oidToNameMapping.put("1.3.14.3.2.22", "RsaKeyTransport");
    oidToNameMapping.put("1.3.14.3.2.23", "Keyed-hash-seal");
    oidToNameMapping.put("1.3.14.3.2.24", "Md2WithRSASignature");
    oidToNameMapping.put("1.3.14.3.2.25", "Md5WithRSASignature");
    oidToNameMapping.put("1.3.14.3.2.26", "Sha1");
    oidToNameMapping.put("1.3.14.3.2.27", "DsaWithSHA1");
    oidToNameMapping.put("1.3.14.3.2.28", "DsaWithCommonSHA1");
    oidToNameMapping.put("1.3.14.3.2.29", "Sha-1WithRSAEncryption");
    oidToNameMapping.put("1.3.14.3.2.3", "Md5WithRSA");
    oidToNameMapping.put("1.3.14.3.2.3.1", "Sqmod-NwithRSA");
    oidToNameMapping.put("1.3.14.3.2.4", "Md4WithRSAEncryption");
    oidToNameMapping.put("1.3.14.3.2.6", "DesECB");
    oidToNameMapping.put("1.3.14.3.2.7", "DesCBC");
    oidToNameMapping.put("1.3.14.3.2.8", "DesOFB");
    oidToNameMapping.put("1.3.14.3.2.9", "DesCFB");
    oidToNameMapping.put("1.3.14.3.3.1", "Simple-strong-auth-mechanism");
    oidToNameMapping.put("1.3.14.7.2.1.1", "ElGamal");
    oidToNameMapping.put("1.3.14.7.2.3.1", "Md2WithRSA");
    oidToNameMapping.put("1.3.14.7.2.3.2", "Md2WithElGamal");
    oidToNameMapping.put("1.3.36.1", "Document");
    oidToNameMapping.put("1.3.36.1.1", "FinalVersion");
    oidToNameMapping.put("1.3.36.1.2", "Draft");
    oidToNameMapping.put("1.3.36.2", "Sio");
    oidToNameMapping.put("1.3.36.2.1", "Sedu");
    oidToNameMapping.put("1.3.36.3", "Algorithm");
    oidToNameMapping.put("1.3.36.3.1", "EncryptionAlgorithm");
    oidToNameMapping.put("1.3.36.3.1.1", "Des");
    oidToNameMapping.put("1.3.36.3.1.1.1", "DesECB_pad");
    oidToNameMapping.put("1.3.36.3.1.1.1.1", "DesECB_ISOpad");
    oidToNameMapping.put("1.3.36.3.1.1.2.1", "DesCBC_pad");
    oidToNameMapping.put("1.3.36.3.1.1.2.1.1", "DesCBC_ISOpad");
    oidToNameMapping.put("1.3.36.3.1.2", "Idea");
    oidToNameMapping.put("1.3.36.3.1.2.1", "IdeaECB");
    oidToNameMapping.put("1.3.36.3.1.2.1.1", "IdeaECB_pad");
    oidToNameMapping.put("1.3.36.3.1.2.1.1.1", "IdeaECB_ISOpad");
    oidToNameMapping.put("1.3.36.3.1.2.2", "IdeaCBC");
    oidToNameMapping.put("1.3.36.3.1.2.2.1", "IdeaCBC_pad");
    oidToNameMapping.put("1.3.36.3.1.2.2.1.1", "IdeaCBC_ISOpad");
    oidToNameMapping.put("1.3.36.3.1.2.3", "IdeaOFB");
    oidToNameMapping.put("1.3.36.3.1.2.4", "IdeaCFB");
    oidToNameMapping.put("1.3.36.3.1.3", "Des_3");
    oidToNameMapping.put("1.3.36.3.1.3.1.1", "Des_3ECB_pad");
    oidToNameMapping.put("1.3.36.3.1.3.1.1.1", "Des_3ECB_ISOpad");
    oidToNameMapping.put("1.3.36.3.1.3.2.1", "Des_3CBC_pad");
    oidToNameMapping.put("1.3.36.3.1.3.2.1.1", "Des_3CBC_ISOpad");
    oidToNameMapping.put("1.3.36.3.1.4", "RsaEncryption");
    oidToNameMapping.put("1.3.36.3.1.4.512.17",
                         "RsaEncryptionWithlmod512expe17");
    oidToNameMapping.put("1.3.36.3.1.5", "Bsi-1");
    oidToNameMapping.put("1.3.36.3.1.5.1", "Bsi_1ECB_pad");
    oidToNameMapping.put("1.3.36.3.1.5.2", "Bsi_1CBC_pad");
    oidToNameMapping.put("1.3.36.3.1.5.2.1", "Bsi_1CBC_PEMpad");
    oidToNameMapping.put("1.3.36.3.2", "HashAlgorithm");
    oidToNameMapping.put("1.3.36.3.2.1", "Ripemd160");
    oidToNameMapping.put("1.3.36.3.2.2", "Ripemd128");
    oidToNameMapping.put("1.3.36.3.2.3", "Ripemd256");
    oidToNameMapping.put("1.3.36.3.2.4", "Mdc2singleLength");
    oidToNameMapping.put("1.3.36.3.2.5", "Mdc2doubleLength");
    oidToNameMapping.put("1.3.36.3.3", "SignatureAlgorithm");
    oidToNameMapping.put("1.3.36.3.3.1", "RsaSignature");
    oidToNameMapping.put("1.3.36.3.3.1.1", "RsaSignatureWithsha1");
    oidToNameMapping.put("1.3.36.3.3.1.1.1024.11",
                         "RsaSignatureWithsha1_l1024_l11");
    oidToNameMapping.put("1.3.36.3.3.1.1.1024.2",
                         "RsaSignatureWithsha1_l1024_l2");
    oidToNameMapping.put("1.3.36.3.3.1.1.1024.3",
                         "RsaSignatureWithsha1_l1024_l3");
    oidToNameMapping.put("1.3.36.3.3.1.1.1024.5",
                         "RsaSignatureWithsha1_l1024_l5");
    oidToNameMapping.put("1.3.36.3.3.1.1.1024.9",
                         "RsaSignatureWithsha1_l1024_l9");
    oidToNameMapping.put("1.3.36.3.3.1.1.512.11",
                         "RsaSignatureWithsha1_l512_l11");
    oidToNameMapping.put("1.3.36.3.3.1.1.512.2",
                         "RsaSignatureWithsha1_l512_l2");
    oidToNameMapping.put("1.3.36.3.3.1.1.512.3",
                         "RsaSignatureWithsha1_l512_l3");
    oidToNameMapping.put("1.3.36.3.3.1.1.512.5",
                         "RsaSignatureWithsha1_l512_l5");
    oidToNameMapping.put("1.3.36.3.3.1.1.512.9",
                         "RsaSignatureWithsha1_l512_l9");
    oidToNameMapping.put("1.3.36.3.3.1.1.640.11",
                         "RsaSignatureWithsha1_l640_l11");
    oidToNameMapping.put("1.3.36.3.3.1.1.640.2",
                         "RsaSignatureWithsha1_l640_l2");
    oidToNameMapping.put("1.3.36.3.3.1.1.640.3",
                         "RsaSignatureWithsha1_l640_l3");
    oidToNameMapping.put("1.3.36.3.3.1.1.640.5",
                         "RsaSignatureWithsha1_l640_l5");
    oidToNameMapping.put("1.3.36.3.3.1.1.640.9",
                         "RsaSignatureWithsha1_l640_l9");
    oidToNameMapping.put("1.3.36.3.3.1.1.768.11",
                         "RsaSignatureWithsha1_l768_l11");
    oidToNameMapping.put("1.3.36.3.3.1.1.768.2",
                         "RsaSignatureWithsha1_l768_l2");
    oidToNameMapping.put("1.3.36.3.3.1.1.768.3",
                         "RsaSignatureWithsha1_l768_l3");
    oidToNameMapping.put("1.3.36.3.3.1.1.768.5",
                         "RsaSignatureWithsha1_l768_l5");
    oidToNameMapping.put("1.3.36.3.3.1.1.768.9",
                         "RsaSignatureWithsha1_l768_l9");
    oidToNameMapping.put("1.3.36.3.3.1.1.896.11",
                         "RsaSignatureWithsha1_l896_l11");
    oidToNameMapping.put("1.3.36.3.3.1.1.896.2",
                         "RsaSignatureWithsha1_l896_l2");
    oidToNameMapping.put("1.3.36.3.3.1.1.896.3",
                         "RsaSignatureWithsha1_l896_l3");
    oidToNameMapping.put("1.3.36.3.3.1.1.896.5",
                         "RsaSignatureWithsha1_l896_l5");
    oidToNameMapping.put("1.3.36.3.3.1.1.896.9",
                         "RsaSignatureWithsha1_l896_l9");
    oidToNameMapping.put("1.3.36.3.3.1.2", "RsaSignatureWithripemd160");
    oidToNameMapping.put("1.3.36.3.3.1.2.1024.11",
                         "RsaSignatureWithripemd160_l1024_l11");
    oidToNameMapping.put("1.3.36.3.3.1.2.1024.2",
                         "RsaSignatureWithripemd160_l1024_l2");
    oidToNameMapping.put("1.3.36.3.3.1.2.1024.3",
                         "RsaSignatureWithripemd160_l1024_l3");
    oidToNameMapping.put("1.3.36.3.3.1.2.1024.5",
                         "RsaSignatureWithripemd160_l1024_l5");
    oidToNameMapping.put("1.3.36.3.3.1.2.1024.9",
                         "RsaSignatureWithripemd160_l1024_l9");
    oidToNameMapping.put("1.3.36.3.3.1.2.512.11",
                         "RsaSignatureWithripemd160_l512_l11");
    oidToNameMapping.put("1.3.36.3.3.1.2.512.2",
                         "RsaSignatureWithripemd160_l512_l2");
    oidToNameMapping.put("1.3.36.3.3.1.2.512.3",
                         "RsaSignatureWithripemd160_l512_l3");
    oidToNameMapping.put("1.3.36.3.3.1.2.512.5",
                         "RsaSignatureWithripemd160_l512_l5");
    oidToNameMapping.put("1.3.36.3.3.1.2.512.9",
                         "RsaSignatureWithripemd160_l512_l9");
    oidToNameMapping.put("1.3.36.3.3.1.2.640.11",
                         "RsaSignatureWithripemd160_l640_l11");
    oidToNameMapping.put("1.3.36.3.3.1.2.640.2",
                         "RsaSignatureWithripemd160_l640_l2");
    oidToNameMapping.put("1.3.36.3.3.1.2.640.3",
                         "RsaSignatureWithripemd160_l640_l3");
    oidToNameMapping.put("1.3.36.3.3.1.2.640.5",
                         "RsaSignatureWithripemd160_l640_l5");
    oidToNameMapping.put("1.3.36.3.3.1.2.640.9",
                         "RsaSignatureWithripemd160_l640_l9");
    oidToNameMapping.put("1.3.36.3.3.1.2.768.11",
                         "RsaSignatureWithripemd160_l768_l11");
    oidToNameMapping.put("1.3.36.3.3.1.2.768.2",
                         "RsaSignatureWithripemd160_l768_l2");
    oidToNameMapping.put("1.3.36.3.3.1.2.768.3",
                         "RsaSignatureWithripemd160_l768_l3");
    oidToNameMapping.put("1.3.36.3.3.1.2.768.5",
                         "RsaSignatureWithripemd160_l768_l5");
    oidToNameMapping.put("1.3.36.3.3.1.2.768.9",
                         "RsaSignatureWithripemd160_l768_l9");
    oidToNameMapping.put("1.3.36.3.3.1.2.896.11",
                         "RsaSignatureWithripemd160_l896_l11");
    oidToNameMapping.put("1.3.36.3.3.1.2.896.2",
                         "RsaSignatureWithripemd160_l896_l2");
    oidToNameMapping.put("1.3.36.3.3.1.2.896.3",
                         "RsaSignatureWithripemd160_l896_l3");
    oidToNameMapping.put("1.3.36.3.3.1.2.896.5",
                         "RsaSignatureWithripemd160_l896_l5");
    oidToNameMapping.put("1.3.36.3.3.1.2.896.9",
                         "RsaSignatureWithripemd160_l896_l9");
    oidToNameMapping.put("1.3.36.3.3.1.3", "RsaSignatureWithrimpemd128");
    oidToNameMapping.put("1.3.36.3.3.1.4", "RsaSignatureWithrimpemd256");
    oidToNameMapping.put("1.3.36.3.3.2", "EcsieSign");
    oidToNameMapping.put("1.3.36.3.3.2.1", "EcsieSignWithsha1");
    oidToNameMapping.put("1.3.36.3.3.2.2", "EcsieSignWithripemd160");
    oidToNameMapping.put("1.3.36.3.3.2.3", "EcsieSignWithmd2");
    oidToNameMapping.put("1.3.36.3.3.2.4", "EcsieSignWithmd5");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.1", "BrainpoolP160r1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.10", "BrainpoolP320t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.11", "BrainpoolP384r1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.12", "BrainpoolP384t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.13", "BrainpoolP512r1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.14", "BrainpoolP512t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.2", "BrainpoolP160t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.3", "BrainpoolP192r1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.4", "BrainpoolP192t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.5", "BrainpoolP224r1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.6", "BrainpoolP224t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.7", "BrainpoolP256r1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.8", "BrainpoolP256t1");
    oidToNameMapping.put("1.3.36.3.3.2.8.1.1.9", "BrainpoolP320r1");
    oidToNameMapping.put("1.3.36.3.4", "SignatureScheme");
    oidToNameMapping.put("1.3.36.3.4.1", "SigS_ISO9796-1");
    oidToNameMapping.put("1.3.36.3.4.2", "SigS_ISO9796-2");
    oidToNameMapping.put("1.3.36.3.4.2.1", "SigS_ISO9796-2Withred");
    oidToNameMapping.put("1.3.36.3.4.2.2", "SigS_ISO9796-2Withrsa");
    oidToNameMapping.put("1.3.36.3.4.2.3", "SigS_ISO9796-2Withrnd");
    oidToNameMapping.put("1.3.36.4", "Attribute");
    oidToNameMapping.put("1.3.36.5", "Policy");
    oidToNameMapping.put("1.3.36.6", "Api");
    oidToNameMapping.put("1.3.36.6.1", "Manufacturer-specific_api");
    oidToNameMapping.put("1.3.36.6.1.1", "Utimaco-api");
    oidToNameMapping.put("1.3.36.6.2", "Functionality-specific_api");
    oidToNameMapping.put("1.3.36.7", "Keymgmnt");
    oidToNameMapping.put("1.3.36.7.1", "Keyagree");
    oidToNameMapping.put("1.3.36.7.1.1", "BsiPKE");
    oidToNameMapping.put("1.3.36.7.2", "Keytrans");
    oidToNameMapping.put("1.3.36.7.2.1", "EncISO9796-2Withrsa");
    oidToNameMapping.put("1.3.36.8.1.1",
                         "Teletrust SigGConform policyIdentifier");
    oidToNameMapping.put("1.3.36.8.2.1", "DirectoryService");
    oidToNameMapping.put("1.3.36.8.3.1", "DateOfCertGen");
    oidToNameMapping.put("1.3.36.8.3.10", "RequestedCertificate");
    oidToNameMapping.put("1.3.36.8.3.11", "NamingAuthorities");
    oidToNameMapping.put("1.3.36.8.3.11.1", "RechtWirtschaftSteuern");
    oidToNameMapping.put("1.3.36.8.3.11.1.1", "Rechtsanwaeltin");
    oidToNameMapping.put("1.3.36.8.3.11.1.10", "NotarVertreterin");
    oidToNameMapping.put("1.3.36.8.3.11.1.11", "NotarVertreter");
    oidToNameMapping.put("1.3.36.8.3.11.1.12", "NotariatsVerwalterin");
    oidToNameMapping.put("1.3.36.8.3.11.1.13", "NotariatsVerwalter");
    oidToNameMapping.put("1.3.36.8.3.11.1.14", "WirtschaftsPrueferin");
    oidToNameMapping.put("1.3.36.8.3.11.1.15", "WirtschaftsPruefer");
    oidToNameMapping.put("1.3.36.8.3.11.1.16", "VereidigteBuchprueferin");
    oidToNameMapping.put("1.3.36.8.3.11.1.17", "VereidigterBuchpruefer");
    oidToNameMapping.put("1.3.36.8.3.11.1.18", "PatentAnwaeltin");
    oidToNameMapping.put("1.3.36.8.3.11.1.19", "PatentAnwalt");
    oidToNameMapping.put("1.3.36.8.3.11.1.2", "Rechtsanwalt");
    oidToNameMapping.put("1.3.36.8.3.11.1.3", "RechtsBeistand");
    oidToNameMapping.put("1.3.36.8.3.11.1.4", "SteuerBeraterin");
    oidToNameMapping.put("1.3.36.8.3.11.1.5", "SteuerBerater");
    oidToNameMapping.put("1.3.36.8.3.11.1.6", "SteuerBevollmaechtigte");
    oidToNameMapping.put("1.3.36.8.3.11.1.7", "SteuerBevollmaechtigter");
    oidToNameMapping.put("1.3.36.8.3.11.1.8", "Notarin");
    oidToNameMapping.put("1.3.36.8.3.11.1.9", "Notar");
    oidToNameMapping.put("1.3.36.8.3.12", "CertInDirSince");
    oidToNameMapping.put("1.3.36.8.3.13", "CertHash");
    oidToNameMapping.put("1.3.36.8.3.14", "NameAtBirth");
    oidToNameMapping.put("1.3.36.8.3.15", "AdditionalInformation");
    oidToNameMapping.put("1.3.36.8.3.2", "Procuration");
    oidToNameMapping.put("1.3.36.8.3.3", "Admission");
    oidToNameMapping.put("1.3.36.8.3.4", "MonetaryLimit");
    oidToNameMapping.put("1.3.36.8.3.5", "DeclarationOfMajority");
    oidToNameMapping.put("1.3.36.8.3.6", "IntegratedCircuitCardSerialNumber");
    oidToNameMapping.put("1.3.36.8.3.7", "PKReference");
    oidToNameMapping.put("1.3.36.8.3.8", "Restriction");
    oidToNameMapping.put("1.3.36.8.3.9", "RetrieveIfAllowed");
    oidToNameMapping.put("1.3.36.8.4.1", "PersonalData");
    oidToNameMapping.put("1.3.36.8.4.8", "Restriction");
    oidToNameMapping.put("1.3.36.8.5.1.1.1", "RsaIndicateSHA1");
    oidToNameMapping.put("1.3.36.8.5.1.1.2", "RsaIndicateRIPEMD160");
    oidToNameMapping.put("1.3.36.8.5.1.1.3", "RsaWithSHA1");
    oidToNameMapping.put("1.3.36.8.5.1.1.4", "RsaWithRIPEMD160");
    oidToNameMapping.put("1.3.36.8.5.1.2.1", "DsaExtended");
    oidToNameMapping.put("1.3.36.8.5.1.2.2", "DsaWithRIPEMD160");
    oidToNameMapping.put("1.3.36.8.6.1", "Cert");
    oidToNameMapping.put("1.3.36.8.6.10", "AutoGen");
    oidToNameMapping.put("1.3.36.8.6.2", "CertRef");
    oidToNameMapping.put("1.3.36.8.6.3", "AttrCert");
    oidToNameMapping.put("1.3.36.8.6.4", "AttrRef");
    oidToNameMapping.put("1.3.36.8.6.5", "FileName");
    oidToNameMapping.put("1.3.36.8.6.6", "StorageTime");
    oidToNameMapping.put("1.3.36.8.6.7", "FileSize");
    oidToNameMapping.put("1.3.36.8.6.8", "Location");
    oidToNameMapping.put("1.3.36.8.6.9", "SigNumber");
    oidToNameMapping.put("1.3.36.8.7.1.1", "PtAdobeILL");
    oidToNameMapping.put("1.3.36.8.7.1.10", "PtCorelPHT");
    oidToNameMapping.put("1.3.36.8.7.1.11", "PtDraw");
    oidToNameMapping.put("1.3.36.8.7.1.12", "PtDVI");
    oidToNameMapping.put("1.3.36.8.7.1.13", "PtEPS");
    oidToNameMapping.put("1.3.36.8.7.1.14", "PtExcel");
    oidToNameMapping.put("1.3.36.8.7.1.15", "PtGEM");
    oidToNameMapping.put("1.3.36.8.7.1.16", "PtGIF");
    oidToNameMapping.put("1.3.36.8.7.1.17", "PtHPGL");
    oidToNameMapping.put("1.3.36.8.7.1.18", "PtJPEG");
    oidToNameMapping.put("1.3.36.8.7.1.19", "PtKodak");
    oidToNameMapping.put("1.3.36.8.7.1.2", "PtAmiPro");
    oidToNameMapping.put("1.3.36.8.7.1.20", "PtLaTeX");
    oidToNameMapping.put("1.3.36.8.7.1.21", "PtLotus");
    oidToNameMapping.put("1.3.36.8.7.1.22", "PtLotusPIC");
    oidToNameMapping.put("1.3.36.8.7.1.23", "PtMacPICT");
    oidToNameMapping.put("1.3.36.8.7.1.24", "PtMacWord");
    oidToNameMapping.put("1.3.36.8.7.1.25", "PtMSWfD");
    oidToNameMapping.put("1.3.36.8.7.1.26", "PtMSWord");
    oidToNameMapping.put("1.3.36.8.7.1.27", "PtMSWord2");
    oidToNameMapping.put("1.3.36.8.7.1.28", "PtMSWord6");
    oidToNameMapping.put("1.3.36.8.7.1.29", "PtMSWord8");
    oidToNameMapping.put("1.3.36.8.7.1.3", "PtAutoCAD");
    oidToNameMapping.put("1.3.36.8.7.1.30", "PtPDF");
    oidToNameMapping.put("1.3.36.8.7.1.31", "PtPIF");
    oidToNameMapping.put("1.3.36.8.7.1.32", "PtPostscript");
    oidToNameMapping.put("1.3.36.8.7.1.33", "PtRTF");
    oidToNameMapping.put("1.3.36.8.7.1.34", "PtSCITEX");
    oidToNameMapping.put("1.3.36.8.7.1.35", "PtTAR");
    oidToNameMapping.put("1.3.36.8.7.1.36", "PtTarga");
    oidToNameMapping.put("1.3.36.8.7.1.37", "PtTeX");
    oidToNameMapping.put("1.3.36.8.7.1.38", "PtText");
    oidToNameMapping.put("1.3.36.8.7.1.39", "PtTIFF");
    oidToNameMapping.put("1.3.36.8.7.1.4", "PtBinary");
    oidToNameMapping.put("1.3.36.8.7.1.40", "PtTIFF-FC");
    oidToNameMapping.put("1.3.36.8.7.1.41", "PtUID");
    oidToNameMapping.put("1.3.36.8.7.1.42", "PtUUEncode");
    oidToNameMapping.put("1.3.36.8.7.1.43", "PtWMF");
    oidToNameMapping.put("1.3.36.8.7.1.44", "PtWordPerfect");
    oidToNameMapping.put("1.3.36.8.7.1.45", "PtWPGrph");
    oidToNameMapping.put("1.3.36.8.7.1.5", "PtBMP");
    oidToNameMapping.put("1.3.36.8.7.1.6", "PtCGM");
    oidToNameMapping.put("1.3.36.8.7.1.7", "PtCorelCRT");
    oidToNameMapping.put("1.3.36.8.7.1.8", "PtCorelDRW");
    oidToNameMapping.put("1.3.36.8.7.1.9", "PtCorelEXC");
    oidToNameMapping.put("1.3.6.1.4.1.11591", "Gnu");
    oidToNameMapping.put("1.3.6.1.4.1.11591.1", "GnuRadius");
    oidToNameMapping.put("1.3.6.1.4.1.11591.12", "GnuDigestAlgorithm");
    oidToNameMapping.put("1.3.6.1.4.1.11591.12.2", "Tiger");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13", "GnuEncryptionAlgorithm");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2", "Serpent");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.1", "Serpent128_ECB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.2", "Serpent128_CBC");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.21", "Serpent192_ECB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.22", "Serpent192_CBC");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.23", "Serpent192_OFB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.24", "Serpent192_CFB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.3", "Serpent128_OFB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.4", "Serpent128_CFB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.41", "Serpent256_ECB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.42", "Serpent256_CBC");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.43", "Serpent256_OFB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.13.2.44", "Serpent256_CFB");
    oidToNameMapping.put("1.3.6.1.4.1.11591.3", "GnuRadar");
    oidToNameMapping.put("1.3.6.1.4.1.14370.1.6", "GeoTrust EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.14777.6.1.1", "Izenpe EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.14777.6.1.2", "Izenpe EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.16334.509.1.1",
                         "Northrop Grumman extKeyUsage?");
    oidToNameMapping.put("1.3.6.1.4.1.16334.509.2.1", "NgcClass1");
    oidToNameMapping.put("1.3.6.1.4.1.16334.509.2.2", "NgcClass2");
    oidToNameMapping.put("1.3.6.1.4.1.16334.509.2.3", "NgcClass3");
    oidToNameMapping.put("1.3.6.1.4.1.17326.10.14.2.1.2",
                         "Camerfirma EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.17326.10.8.12.1.2",
                         "Camerfirma EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.188.7.1.1", "Ascom");
    oidToNameMapping.put("1.3.6.1.4.1.188.7.1.1.1", "IdeaECB");
    oidToNameMapping.put("1.3.6.1.4.1.188.7.1.1.2", "IdeaCBC");
    oidToNameMapping.put("1.3.6.1.4.1.188.7.1.1.3", "IdeaCFB");
    oidToNameMapping.put("1.3.6.1.4.1.188.7.1.1.4", "IdeaOFB");
    oidToNameMapping.put("1.3.6.1.4.1.22234.2.5.2.3.1", "CertPlus EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.23223.1.1.1", "StartCom EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.23629.1.4.2.1.1", "SafenetUsageLimit");
    oidToNameMapping.put("1.3.6.1.4.1.23629.1.4.2.1.2", "SafenetEndDate");
    oidToNameMapping.put("1.3.6.1.4.1.23629.1.4.2.1.3", "SafenetStartDate");
    oidToNameMapping.put("1.3.6.1.4.1.23629.1.4.2.1.4", "SafenetAdminCert");
    oidToNameMapping.put("1.3.6.1.4.1.23629.1.4.2.2.1", "SafenetKeyDigest");
    oidToNameMapping.put("1.3.6.1.4.1.2428.10.1.1", "UNINETT policyIdentifier");
    oidToNameMapping.put("1.3.6.1.4.1.2712.10", "ICE-TEL policyIdentifier");
    oidToNameMapping.put("1.3.6.1.4.1.2786.1.1.1",
                         "ICE-TEL Italian policyIdentifier");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.1.1", "BlowfishECB");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.1.2", "BlowfishCBC");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.1.3", "BlowfishCFB");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.1.4", "BlowfishOFB");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.2.1", "Elgamal");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.2.1.1", "ElgamalWithSHA-1");
    oidToNameMapping.put("1.3.6.1.4.1.3029.1.2.1.2", "ElgamalWithRIPEMD-160");
    oidToNameMapping.put("1.3.6.1.4.1.3029.3.1.1", "CryptlibPresenceCheck");
    oidToNameMapping.put("1.3.6.1.4.1.3029.3.1.2", "PkiBoot");
    oidToNameMapping.put("1.3.6.1.4.1.3029.3.1.4", "CrlExtReason");
    oidToNameMapping.put("1.3.6.1.4.1.3029.3.1.5", "KeyFeatures");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1", "CryptlibContent");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1.1", "CryptlibConfigData");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1.2", "CryptlibUserIndex");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1.3", "CryptlibUserInfo");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1.4", "RtcsRequest");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1.5", "RtcsResponse");
    oidToNameMapping.put("1.3.6.1.4.1.3029.4.1.6", "RtcsResponseExt");
    oidToNameMapping.put("1.3.6.1.4.1.3029.42.11172.1", "Mpeg-1");
    oidToNameMapping.put("1.3.6.1.4.1.3029.54.11940.54",
                         "TSA policy 'Anything that arrives, we sign'");
    oidToNameMapping.put("1.3.6.1.4.1.3029.88.89.90.90.89",
                         "XYZZY policyIdentifier");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.1", "CertTrustList");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.1.1", "SortedCtl");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.10.1", "CmcAddAttributes");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.11", "CertPropIdPrefix");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.11.20", "CertKeyIdentifierPropId");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.11.28",
                         "CertIssuerSerialNumberMd5HashPropId");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.11.29",
                         "CertSubjectNameMd5HashPropId");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.11.4", "CertMd5HashPropId");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.12.1", "AnyApplicationPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.2", "NextUpdateLocation");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.1", "CertTrustListSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.10", "QualifiedSubordination");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.11", "KeyRecovery");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.12", "DocumentSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.13", "LifetimeSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.14", "MobileDeviceSoftware");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.15", "SmartDisplay");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.16", "CspSignature");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.2", "TimeStampSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.3", "ServerGatedCrypto");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.3.1", "Serialized");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.4", "EncryptedFileSystem");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.4.1", "EfsRecovery");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.5", "WhqlCrypto");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.6", "Nt5Crypto");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.7", "OemWHQLCrypto");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.8", "EmbeddedNTCrypto");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.3.9", "RootListSigner");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.4.1", "YesnoTrustAttr");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.5.1", "Drm");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.5.2", "DrmIndividualization");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.6.1", "Licenses");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.6.2", "LicenseServer");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.7.1", "KeyidRdn");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.8.1", "RemoveCertificate");
    oidToNameMapping.put("1.3.6.1.4.1.311.10.9.1", "CrossCertDistPoints");
    oidToNameMapping.put("1.3.6.1.4.1.311.13.1", "RenewalCertificate");
    oidToNameMapping.put("1.3.6.1.4.1.311.13.2.1", "EnrolmentNameValuePair");
    oidToNameMapping.put("1.3.6.1.4.1.311.13.2.2", "EnrolmentCSP");
    oidToNameMapping.put("1.3.6.1.4.1.311.13.2.3", "OsVersion");
    oidToNameMapping.put("1.3.6.1.4.1.311.16.4", "MicrosoftRecipientInfo");
    oidToNameMapping.put("1.3.6.1.4.1.311.17.1", "Pkcs12KeyProviderNameAttr");
    oidToNameMapping.put("1.3.6.1.4.1.311.17.2", "LocalMachineKeyset");
    oidToNameMapping.put("1.3.6.1.4.1.311.17.3", "Pkcs12ExtendedAttributes");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.10", "SpcAgencyInfo");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.11", "SpcStatementType");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.12", "SpcSpOpusInfo");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.14", "CertReqExtensions");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.15", "SpcPEImageData");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.18", "SpcRawFileData");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.19", "SpcStructuredStorageData");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.20", "SpcJavaClassData (type.1)");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.21", "IndividualCodeSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.22", "CommercialCodeSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.25", "SpcLink (type.2)");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.26", "SpcMinimalCriteriaInfo");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.27", "SpcFinancialCriteriaInfo");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.28", "SpcLink (type.3)");
    oidToNameMapping.put("1.3.6.1.4.1.311.2.1.4", "SpcIndirectDataContext");
    oidToNameMapping.put("1.3.6.1.4.1.311.20.1", "AutoEnrollCtlUsage");
    oidToNameMapping.put("1.3.6.1.4.1.311.20.2", "EnrollCerttypeExtension");
    oidToNameMapping.put("1.3.6.1.4.1.311.20.2.1", "EnrollmentAgent");
    oidToNameMapping.put("1.3.6.1.4.1.311.20.2.2", "SmartcardLogon");
    oidToNameMapping.put("1.3.6.1.4.1.311.20.2.3", "UserPrincipalName");
    oidToNameMapping.put("1.3.6.1.4.1.311.20.3", "CertManifold");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.1", "CAKeyCertIndexPair");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.10", "ApplicationCertPolicies");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.11", "ApplicationPolicyMappings");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.12",
                         "ApplicationPolicyConstraints");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.13", "ArchivedKey");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.14", "CrlSelfCDP");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.15", "RequireCertChainPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.16", "ArchivedKeyCertHash");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.17", "IssuedCertHash");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.19", "DsEmailReplication");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.2", "CertSrvPreviousCertHash");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.20", "RequestClientInfo");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.21", "EncryptedKeyHash");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.22", "CertsrvCrossCaVersion");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.3", "CrlVirtualBase");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.4", "CrlNextPublish");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.5", "CaExchange");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.6", "KeyRecovery");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.7", "CertificateTemplate");
    oidToNameMapping.put("1.3.6.1.4.1.311.21.9", "RdnDummySigner");
    oidToNameMapping.put("1.3.6.1.4.1.311.25.1", "NtdsReplication");
    oidToNameMapping.put("1.3.6.1.4.1.311.3.2.1", "TimestampRequest");
    oidToNameMapping.put("1.3.6.1.4.1.311.31.1", "ProductUpdate");
    oidToNameMapping.put("1.3.6.1.4.1.311.47.1.1", "SystemHealth");
    oidToNameMapping.put("1.3.6.1.4.1.311.47.1.3", "SystemHealthLoophole");
    oidToNameMapping.put("1.3.6.1.4.1.311.60.1.1", "RootProgramFlags");
    oidToNameMapping.put("1.3.6.1.4.1.311.60.2.1.1",
                         "JurisdictionOfIncorporationL");
    oidToNameMapping.put("1.3.6.1.4.1.311.60.2.1.2",
                         "JurisdictionOfIncorporationSP");
    oidToNameMapping.put("1.3.6.1.4.1.311.60.2.1.3",
                         "JurisdictionOfIncorporationC");
    oidToNameMapping.put("1.3.6.1.4.1.311.61.1.1", "KernelModeCodeSigning");
    oidToNameMapping.put("1.3.6.1.4.1.311.88.2.1", "OriginalFilename");
    oidToNameMapping.put("1.3.6.1.4.1.3401.8.1.1", "PgpExtension");
    oidToNameMapping.put("1.3.6.1.4.1.34697.2.1", "AffirmTrust EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.34697.2.2", "AffirmTrust EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.34697.2.3", "AffirmTrust EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.34697.2.4", "AffirmTrust EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.3576.7", "EciaAscX12Edi");
    oidToNameMapping.put("1.3.6.1.4.1.3576.7.1", "PlainEDImessage");
    oidToNameMapping.put("1.3.6.1.4.1.3576.7.2", "SignedEDImessage");
    oidToNameMapping.put("1.3.6.1.4.1.3576.7.5", "IntegrityEDImessage");
    oidToNameMapping.put("1.3.6.1.4.1.3576.7.65", "IaReceiptMessage");
    oidToNameMapping.put("1.3.6.1.4.1.3576.7.97", "IaStatusMessage");
    oidToNameMapping.put("1.3.6.1.4.1.3576.8", "EciaEdifact");
    oidToNameMapping.put("1.3.6.1.4.1.3576.9", "EciaNonEdi");
    oidToNameMapping.put("1.3.6.1.4.1.40869.1.1.22.3", "TWCA EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.4146", "Globalsign");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1", "GlobalsignPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.1", "GlobalSign EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.10", "GlobalsignDVPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.20", "GlobalsignOVPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.30", "GlobalsignTSAPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.40", "GlobalsignClientCertPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.50", "GlobalsignCodeSignPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.60", "GlobalsignRootSignPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.70",
                         "GlobalsignTrustedRootPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.80", "GlobalsignEDIClientPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.81", "GlobalsignEDIServerPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.90", "GlobalsignTPMRootPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4146.1.95", "GlobalsignOCSPPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.4788.2.202.1", "D-TRUST EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.5309.1", "EdelWebPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.5309.1.2", "EdelWebCustomerPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.5309.1.2.1", "EdelWebClepsydrePolicy");
    oidToNameMapping.put("1.3.6.1.4.1.5309.1.2.2",
                         "EdelWebExperimentalTSAPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.5309.1.2.3",
                         "EdelWebOpenEvidenceTSAPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.5472", "Timeproof");
    oidToNameMapping.put("1.3.6.1.4.1.5472.1", "Tss");
    oidToNameMapping.put("1.3.6.1.4.1.5472.1.1", "Tss80");
    oidToNameMapping.put("1.3.6.1.4.1.5472.1.2", "Tss380");
    oidToNameMapping.put("1.3.6.1.4.1.5472.1.3", "Tss400");
    oidToNameMapping.put("1.3.6.1.4.1.5770.0.3", "SecondaryPractices");
    oidToNameMapping.put("1.3.6.1.4.1.5770.0.4", "PhysicianIdentifiers");
    oidToNameMapping.put("1.3.6.1.4.1.6334.1.100.1", "Cybertrust EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.6449.1.2.1.3.1", "ComodoPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.6449.1.2.1.5.1", "Comodo EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.6449.1.2.2.15", "WotrustPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.6449.1.3.5.2",
                         "ComodoCertifiedDeliveryService");
    oidToNameMapping.put("1.3.6.1.4.1.6449.2.1.1", "ComodoTimestampingPolicy");
    oidToNameMapping.put("1.3.6.1.4.1.782.1.2.1.8.1",
                         "Network Solutions EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.7879.13.24.1", "T-TeleSec EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.8024.0.2.100.1.2", "QuoVadis EV policy");
    oidToNameMapping.put("1.3.6.1.4.1.8231.1", "RolUnicoNacional");
    oidToNameMapping.put("1.3.6.1.4.1.8301.3.5.1", "ValidityModelChain");
    oidToNameMapping.put("1.3.6.1.4.1.8301.3.5.2", "ValidityModelShell");
    oidToNameMapping.put("1.3.6.1.5.5.7", "Pkix");
    oidToNameMapping.put("1.3.6.1.5.5.7.0.12", "AttributeCert");
    oidToNameMapping.put("1.3.6.1.5.5.7.1", "PrivateExtension");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.1", "AuthorityInfoAccess");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.10", "AcProxying");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.11", "SubjectInfoAccess");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.12", "LogoType");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.13", "WlanSSID");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.2", "BiometricInfo");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.3", "QcStatements");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.4", "AcAuditIdentity");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.5", "AcTargeting");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.6", "AcAaControls");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.7", "IpAddrBlocks");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.8", "AutonomousSysIds");
    oidToNameMapping.put("1.3.6.1.5.5.7.1.9", "RouterIdentifier");
    oidToNameMapping.put("1.3.6.1.5.5.7.10", "AttributeCertificate");
    oidToNameMapping.put("1.3.6.1.5.5.7.10.1", "AuthenticationInfo");
    oidToNameMapping.put("1.3.6.1.5.5.7.10.2", "AccessIdentity");
    oidToNameMapping.put("1.3.6.1.5.5.7.10.3", "ChargingIdentity");
    oidToNameMapping.put("1.3.6.1.5.5.7.10.4", "Group");
    oidToNameMapping.put("1.3.6.1.5.5.7.10.5", "Role");
    oidToNameMapping.put("1.3.6.1.5.5.7.10.6", "WlanSSID");
    oidToNameMapping.put("1.3.6.1.5.5.7.11", "PersonalData");
    oidToNameMapping.put("1.3.6.1.5.5.7.11.1", "PkixQCSyntax-v1");
    oidToNameMapping.put("1.3.6.1.5.5.7.14.2", "ResourceCertificatePolicy");
    oidToNameMapping.put("1.3.6.1.5.5.7.2", "PolicyQualifierIds");
    oidToNameMapping.put("1.3.6.1.5.5.7.2.1", "Cps");
    oidToNameMapping.put("1.3.6.1.5.5.7.2.2", "Unotice");
    oidToNameMapping.put("1.3.6.1.5.5.7.2.3", "TextNotice");
    oidToNameMapping.put("1.3.6.1.5.5.7.20", "Logo");
    oidToNameMapping.put("1.3.6.1.5.5.7.20.1", "LogoLoyalty");
    oidToNameMapping.put("1.3.6.1.5.5.7.20.2", "LogoBackground");
    oidToNameMapping.put("1.3.6.1.5.5.7.3", "KeyPurpose");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.1", "ServerAuth");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.10", "Dvcs");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.11", "SbgpCertAAServerAuth");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.13", "EapOverPPP");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.14", "EapOverLAN");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.2", "ClientAuth");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.3", "CodeSigning");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.4", "EmailProtection");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.5", "IpsecEndSystem");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.6", "IpsecTunnel");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.7", "IpsecUser");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.8", "TimeStamping");
    oidToNameMapping.put("1.3.6.1.5.5.7.3.9", "OcspSigning");
    oidToNameMapping.put("1.3.6.1.5.5.7.4", "CmpInformationTypes");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.1", "CaProtEncCert");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.10", "KeyPairParamReq");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.11", "KeyPairParamRep");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.12", "RevPassphrase");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.13", "ImplicitConfirm");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.14", "ConfirmWaitTime");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.15", "OrigPKIMessage");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.16", "SuppLangTags");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.2", "SignKeyPairTypes");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.3", "EncKeyPairTypes");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.4", "PreferredSymmAlg");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.5", "CaKeyUpdateInfo");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.6", "CurrentCRL");
    oidToNameMapping.put("1.3.6.1.5.5.7.4.7", "UnsupportedOIDs");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1", "Ocsp");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.1", "OcspBasic");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.2", "OcspNonce");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.3", "OcspCRL");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.4", "OcspResponse");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.5", "OcspNoCheck");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.6", "OcspArchiveCutoff");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.1.7", "OcspServiceLocator");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.10", "RpkiManifest");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.11", "SignedObject");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.2", "CaIssuers");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.3", "TimeStamping");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.4", "Dvcs");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.5", "CaRepository");
    oidToNameMapping.put("1.3.6.1.5.5.7.48.7", "SignedObjectRepository");
    oidToNameMapping.put("1.3.6.1.5.5.7.5", "CrmfRegistration");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1", "RegCtrl");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.1", "RegToken");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.2", "Authenticator");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.3", "PkiPublicationInfo");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.4", "PkiArchiveOptions");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.5", "OldCertID");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.6", "ProtocolEncrKey");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.7", "AltCertTemplate");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.1.8", "WtlsTemplate");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.2", "Utf8Pairs");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.2.1", "Utf8Pairs");
    oidToNameMapping.put("1.3.6.1.5.5.7.5.2.2", "CertReq");
    oidToNameMapping.put("1.3.6.1.5.5.7.6", "Algorithms");
    oidToNameMapping.put("1.3.6.1.5.5.7.6.1", "Des40");
    oidToNameMapping.put("1.3.6.1.5.5.7.6.2", "NoSignature");
    oidToNameMapping.put("1.3.6.1.5.5.7.6.3", "Dh-sig-hmac-sha1");
    oidToNameMapping.put("1.3.6.1.5.5.7.6.4", "Dh-pop");
    oidToNameMapping.put("1.3.6.1.5.5.7.7", "CmcControls");
    oidToNameMapping.put("1.3.6.1.5.5.7.8", "OtherNames");
    oidToNameMapping.put("1.3.6.1.5.5.7.8.1", "PersonalData");
    oidToNameMapping.put("1.3.6.1.5.5.7.8.2", "UserGroup");
    oidToNameMapping.put("1.3.6.1.5.5.7.8.5", "XmppAddr");
    oidToNameMapping.put("1.3.6.1.5.5.7.9", "PersonalData");
    oidToNameMapping.put("1.3.6.1.5.5.7.9.1", "DateOfBirth");
    oidToNameMapping.put("1.3.6.1.5.5.7.9.2", "PlaceOfBirth");
    oidToNameMapping.put("1.3.6.1.5.5.7.9.3", "Gender");
    oidToNameMapping.put("1.3.6.1.5.5.7.9.4", "CountryOfCitizenship");
    oidToNameMapping.put("1.3.6.1.5.5.7.9.5", "CountryOfResidence");
    oidToNameMapping.put("1.3.6.1.5.5.8.1.1", "HmacMD5");
    oidToNameMapping.put("1.3.6.1.5.5.8.1.2", "HmacSHA");
    oidToNameMapping.put("1.3.6.1.5.5.8.1.3", "HmacTiger");
    oidToNameMapping.put("1.3.6.1.5.5.8.2.2", "IKEIntermediate");
    oidToNameMapping.put("2.16.528.1.1001.1.1.1.12.6.1.1.1",
                         "DigiNotar EV policy");
    oidToNameMapping.put("2.16.578.1.26.1.3.3", "BuyPass EV policy");
    oidToNameMapping.put("2.16.724.1.2.2.4.1", "PersonalDataInfo");
    oidToNameMapping.put("2.16.756.1.89.1.2.1.1", "SwissSign EV policy");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.1", "SdnsSignatureAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.10",
                         "FortezzaKeyManagementAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.11", "SdnsKMandSigAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.12",
                         "FortezzaKMandSigAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.13", "SuiteASignatureAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.14",
                         "SuiteAConfidentialityAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.15", "SuiteAIntegrityAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.16",
                         "SuiteATokenProtectionAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.17",
                         "SuiteAKeyManagementAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.18", "SuiteAKMandSigAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.19",
                         "FortezzaUpdatedSigAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.2",
                         "FortezzaSignatureAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.20",
                         "FortezzaKMandUpdSigAlgorithms");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.21",
                         "FortezzaUpdatedIntegAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.22", "KeyExchangeAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.23", "FortezzaWrap80Algorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.24",
                         "KEAKeyEncryptionAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.3",
                         "SdnsConfidentialityAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.4",
                         "FortezzaConfidentialityAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.5", "SdnsIntegrityAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.6",
                         "FortezzaIntegrityAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.7",
                         "SdnsTokenProtectionAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.8",
                         "FortezzaTokenProtectionAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.1.9",
                         "SdnsKeyManagementAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.2.1.10.1", "SigPrivileges");
    oidToNameMapping.put("2.16.840.1.101.2.1.10.2", "KmPrivileges");
    oidToNameMapping.put("2.16.840.1.101.2.1.10.3", "NamedTagSetPrivilege");
    oidToNameMapping.put("2.16.840.1.101.2.1.11.1", "UkDemo");
    oidToNameMapping.put("2.16.840.1.101.2.1.11.2", "UsDODClass2");
    oidToNameMapping.put("2.16.840.1.101.2.1.11.3", "UsMediumPilot");
    oidToNameMapping.put("2.16.840.1.101.2.1.11.4", "UsDODClass4");
    oidToNameMapping.put("2.16.840.1.101.2.1.11.5", "UsDODClass3");
    oidToNameMapping.put("2.16.840.1.101.2.1.11.6", "UsDODClass5");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0", "TestSecurityPolicy");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.1", "Tsp1");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.1.0",
                         "Tsp1SecurityCategories");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.1.0.0", "Tsp1TagSetZero");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.1.0.1", "Tsp1TagSetOne");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.1.0.2", "Tsp1TagSetTwo");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.2", "Tsp2");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.2.0",
                         "Tsp2SecurityCategories");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.2.0.0", "Tsp2TagSetZero");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.2.0.1", "Tsp2TagSetOne");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.2.0.2", "Tsp2TagSetTwo");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.3", "Kafka");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.3.0",
                         "KafkaSecurityCategories");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.3.0.1", "KafkaTagSetName1");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.3.0.2", "KafkaTagSetName2");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.0.3.0.3", "KafkaTagSetName3");
    oidToNameMapping.put("2.16.840.1.101.2.1.12.1.1", "Tcp1");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.1", "Rfc822MessageFormat");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.2", "EmptyContent");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.3", "CspContentType");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.42", "MspRev3ContentType");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.48", "MspContentType");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.49", "MspRekeyAgentProtocol");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.50", "MspMMP");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.66", "MspRev3-1ContentType");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.72",
                         "ForwardedMSPMessageBodyPart");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.73",
                         "MspForwardedMessageParameters");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.74", "ForwardedCSPMsgBodyPart");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.75",
                         "CspForwardedMessageParameters");
    oidToNameMapping.put("2.16.840.1.101.2.1.2.76", "MspMMP2");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.1", "SdnsSecurityPolicy");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10", "SiSecurityPolicy");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.0", "SiNASP");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.1", "SiELCO");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.10", "SiREL_UK");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.11", "SiREL-NZ");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.12", "SiGeneric");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.2", "SiTK");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.3", "SiDSAP");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.4", "SiSSSS");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.5", "SiDNASP");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.6", "SiBYEMAN");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.7", "SiREL-US");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.8", "SiREL-AUS");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.10.9", "SiREL-CAN");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.11", "Genser");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.11.0", "GenserNations");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.11.1", "GenserComsec");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.11.2", "GenserAcquisition");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.11.3",
                         "GenserSecurityCategories");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.11.3.0", "GenserTagSetName");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.12", "DefaultSecurityPolicy");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.13", "CapcoMarkings");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.13.0",
                         "CapcoSecurityCategories");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.13.0.1", "CapcoTagSetName1");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.13.0.2", "CapcoTagSetName2");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.13.0.3", "CapcoTagSetName3");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.13.0.4", "CapcoTagSetName4");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.2", "SdnsPRBAC");
    oidToNameMapping.put("2.16.840.1.101.2.1.3.3", "MosaicPRBAC");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.1",
                         "SdnsKeyManagementCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.10", "AuxiliaryVector");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.11", "MlReceiptPolicy");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.12", "MlMembership");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.13", "MlAdministrators");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.14", "Alid");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.2",
                         "SdnsUserSignatureCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.20", "JanUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.21", "FebUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.22", "MarUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.23", "AprUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.24", "MayUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.25", "JunUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.26", "JulUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.27", "AugUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.28", "SepUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.29", "OctUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.3", "SdnsKMandSigCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.30", "NovUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.31", "DecUKMs");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.4",
                         "FortezzaKeyManagementCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.40", "MetaSDNSckl");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.41", "SdnsCKL");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.42", "MetaSDNSsignatureCKL");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.43", "SdnsSignatureCKL");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.44",
                         "SdnsCertificateRevocationList");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.45",
                         "FortezzaCertificateRevocationList");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.46", "FortezzaCKL");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.47",
                         "AlExemptedAddressProcessor");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.48", "Guard");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.49", "AlgorithmsSupported");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.5",
                         "FortezzaKMandSigCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.50",
                         "SuiteAKeyManagementCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.51",
                         "SuiteAKMandSigCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.52",
                         "SuiteAUserSignatureCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.53", "PrbacInfo");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.54", "PrbacCAConstraints");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.55", "SigOrKMPrivileges");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.56", "CommPrivileges");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.57", "LabeledAttribute");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.58", "PolicyInformationFile");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.59", "SecPolicyInformationFile");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.6",
                         "FortezzaUserSignatureCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.60", "CAClearanceConstraint");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.7",
                         "FortezzaCASignatureCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.5.8",
                         "SdnsCASignatureCertificate");
    oidToNameMapping.put("2.16.840.1.101.2.1.7.1", "CspExtns");
    oidToNameMapping.put("2.16.840.1.101.2.1.7.1.0", "CspCsExtn");
    oidToNameMapping.put("2.16.840.1.101.2.1.8.1", "MISSISecurityCategories");
    oidToNameMapping.put("2.16.840.1.101.2.1.8.2",
                         "StandardSecurityLabelPrivileges");
    oidToNameMapping.put("2.16.840.1.101.3.1", "Slabel");
    oidToNameMapping.put("2.16.840.1.101.3.2", "Pki");
    oidToNameMapping.put("2.16.840.1.101.3.2.1", "NIST policyIdentifier");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.3.1", "FbcaRudimentaryPolicy");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.3.2", "FbcaBasicPolicy");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.3.3", "FbcaMediumPolicy");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.3.4", "FbcaHighPolicy");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.48.1", "NistTestPolicy1");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.48.2", "NistTestPolicy2");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.48.3", "NistTestPolicy3");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.48.4", "NistTestPolicy4");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.48.5", "NistTestPolicy5");
    oidToNameMapping.put("2.16.840.1.101.3.2.1.48.6", "NistTestPolicy6");
    oidToNameMapping.put("2.16.840.1.101.3.2.2", "Gak");
    oidToNameMapping.put("2.16.840.1.101.3.2.2.1", "KRAKey");
    oidToNameMapping.put("2.16.840.1.101.3.2.3", "Extensions");
    oidToNameMapping.put("2.16.840.1.101.3.2.3.1", "KRTechnique");
    oidToNameMapping.put("2.16.840.1.101.3.2.3.2", "KRecoveryCapable");
    oidToNameMapping.put("2.16.840.1.101.3.2.3.3", "KR");
    oidToNameMapping.put("2.16.840.1.101.3.2.4", "KeyRecoverySchemes");
    oidToNameMapping.put("2.16.840.1.101.3.2.5", "Krapola");
    oidToNameMapping.put("2.16.840.1.101.3.3", "Arpa");
    oidToNameMapping.put("2.16.840.1.101.3.4", "NistAlgorithm");
    oidToNameMapping.put("2.16.840.1.101.3.4.1", "Aes");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.1", "Aes128-ECB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.2", "Aes128-CBC");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.21", "Aes192-ECB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.22", "Aes192-CBC");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.23", "Aes192-OFB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.24", "Aes192-CFB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.25", "Aes192-wrap");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.26", "Aes192-GCM");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.27", "Aes192-CCM");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.28", "Aes192-wrap-pad");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.3", "Aes128-OFB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.4", "Aes128-CFB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.41", "Aes256-ECB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.42", "Aes256-CBC");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.43", "Aes256-OFB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.44", "Aes256-CFB");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.45", "Aes256-wrap");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.46", "Aes256-GCM");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.47", "Aes256-CCM");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.48", "Aes256-wrap-pad");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.5", "Aes128-wrap");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.6", "Aes128-GCM");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.7", "Aes128-CCM");
    oidToNameMapping.put("2.16.840.1.101.3.4.1.8", "Aes128-wrap-pad");
    oidToNameMapping.put("2.16.840.1.101.3.4.2", "HashAlgos");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.1", "Sha-256");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.10", "SHA3-512");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.11", "SHAKE128");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.12", "SHAKE256");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.2", "Sha-384");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.3", "Sha-512");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.4", "Sha-224");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.7", "SHA3-224");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.8", "SHA3-256");
    oidToNameMapping.put("2.16.840.1.101.3.4.2.9", "SHA3-384");
    oidToNameMapping.put("2.16.840.1.101.3.4.3.1", "DsaWithSha224");
    oidToNameMapping.put("2.16.840.1.101.3.4.3.2", "DsaWithSha256");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8", "NovellAlgorithm");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.130", "Md4Packet");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.131", "RsaEncryptionBsafe1");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.132", "NwPassword");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.133", "NovellObfuscate-1");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.22", "DesCbcIV8");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.23", "DesCbcPadIV8");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.24", "DesEDE2CbcIV8");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.25", "DesEDE2CbcPadIV8");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.26", "DesEDE3CbcIV8");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.27", "DesEDE3CbcPadIV8");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.28", "Rc5CbcPad");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.29",
                         "Md2WithRSAEncryptionBSafe1");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.30",
                         "Md5WithRSAEncryptionBSafe1");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.31",
                         "Sha1WithRSAEncryptionBSafe1");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.32", "LmDigest");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.40", "Md2");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.50", "Md5");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.51", "IkeHmacWithSHA1-RSA");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.52", "IkeHmacWithMD5-RSA");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.69", "Rc2CbcPad");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.82", "Sha-1");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.92", "Rc2BSafe1Cbc");
    oidToNameMapping.put("2.16.840.1.113719.1.2.8.95", "Md4");
    oidToNameMapping.put("2.16.840.1.113719.1.9", "Pki");
    oidToNameMapping.put("2.16.840.1.113719.1.9.4", "PkiAttributeType");
    oidToNameMapping.put("2.16.840.1.113719.1.9.4.1", "SecurityAttributes");
    oidToNameMapping.put("2.16.840.1.113719.1.9.4.2", "RelianceLimit");
    oidToNameMapping.put("2.16.840.1.113730.1", "Cert-extension");
    oidToNameMapping.put("2.16.840.1.113730.1.1", "Netscape-cert-type");
    oidToNameMapping.put("2.16.840.1.113730.1.10", "EntityLogo");
    oidToNameMapping.put("2.16.840.1.113730.1.11", "UserPicture");
    oidToNameMapping.put("2.16.840.1.113730.1.12", "Netscape-ssl-server-name");
    oidToNameMapping.put("2.16.840.1.113730.1.13", "Netscape-");
    oidToNameMapping.put("2.16.840.1.113730.1.2", "Netscape-base-url");
    oidToNameMapping.put("2.16.840.1.113730.1.3", "Netscape-revocation-url");
    oidToNameMapping.put("2.16.840.1.113730.1.4", "Netscape-ca-revocation-url");
    oidToNameMapping.put("2.16.840.1.113730.1.7", "Netscape-cert-renewal-url");
    oidToNameMapping.put("2.16.840.1.113730.1.8", "Netscape-ca-policy-url");
    oidToNameMapping.put("2.16.840.1.113730.1.9", "HomePage-url");
    oidToNameMapping.put("2.16.840.1.113730.2", "Data-type");
    oidToNameMapping.put("2.16.840.1.113730.2.1", "DataGIF");
    oidToNameMapping.put("2.16.840.1.113730.2.2", "DataJPEG");
    oidToNameMapping.put("2.16.840.1.113730.2.3", "DataURL");
    oidToNameMapping.put("2.16.840.1.113730.2.4", "DataHTML");
    oidToNameMapping.put("2.16.840.1.113730.2.5", "CertSequence");
    oidToNameMapping.put("2.16.840.1.113730.2.6", "CertURL");
    oidToNameMapping.put("2.16.840.1.113730.3", "Directory");
    oidToNameMapping.put("2.16.840.1.113730.3.1", "LdapDefinitions");
    oidToNameMapping.put("2.16.840.1.113730.3.1.1", "CarLicense");
    oidToNameMapping.put("2.16.840.1.113730.3.1.2", "DepartmentNumber");
    oidToNameMapping.put("2.16.840.1.113730.3.1.3", "EmployeeNumber");
    oidToNameMapping.put("2.16.840.1.113730.3.1.4", "EmployeeType");
    oidToNameMapping.put("2.16.840.1.113730.3.2.2", "InetOrgPerson");
    oidToNameMapping.put("2.16.840.1.113730.4.1", "ServerGatedCrypto");
    oidToNameMapping.put("2.16.840.1.113733.1", "Pki");
    oidToNameMapping.put("2.16.840.1.113733.1.6.11",
                         "VerisignOnsiteJurisdictionHash");
    oidToNameMapping.put("2.16.840.1.113733.1.6.13",
                         "Unknown Verisign VPN extension");
    oidToNameMapping.put("2.16.840.1.113733.1.6.15", "VerisignServerID");
    oidToNameMapping.put("2.16.840.1.113733.1.6.3", "VerisignCZAG");
    oidToNameMapping.put("2.16.840.1.113733.1.6.6", "VerisignInBox");
    oidToNameMapping.put("2.16.840.1.113733.1.7.1.1",
                         "VerisignCertPolicies95Qualifier1");
    oidToNameMapping.put("2.16.840.1.113733.1.7.1.1.1", "VerisignCPSv1notice");
    oidToNameMapping.put("2.16.840.1.113733.1.7.1.1.2", "VerisignCPSv1nsi");
    oidToNameMapping.put("2.16.840.1.113733.1.7.23.6", "VeriSign EV policy");
    oidToNameMapping.put("2.16.840.1.113733.1.7.48.1", "Thawte EV policy");
    oidToNameMapping.put("2.16.840.1.113733.1.8.1", "VerisignISSStrongCrypto");
    oidToNameMapping.put("2.16.840.1.113733.1.9", "Pkcs7Attribute");
    oidToNameMapping.put("2.16.840.1.113733.1.9.2", "MessageType");
    oidToNameMapping.put("2.16.840.1.113733.1.9.3", "PkiStatus");
    oidToNameMapping.put("2.16.840.1.113733.1.9.4", "FailInfo");
    oidToNameMapping.put("2.16.840.1.113733.1.9.5", "SenderNonce");
    oidToNameMapping.put("2.16.840.1.113733.1.9.6", "RecipientNonce");
    oidToNameMapping.put("2.16.840.1.113733.1.9.7", "TransID");
    oidToNameMapping.put("2.16.840.1.113733.1.9.8", "ExtensionReq");
    oidToNameMapping.put("2.16.840.1.113741.2", "IntelCDSA");
    oidToNameMapping.put("2.16.840.1.114028.10.1.2", "Entrust EV policy");
    oidToNameMapping.put("2.16.840.1.114171.500.9", "Wells Fargo EV policy");
    oidToNameMapping.put("2.16.840.1.114404.1.1.2.4.1", "TrustWave EV policy");
    oidToNameMapping.put("2.16.840.1.114412.1", "DigiCertNonEVCerts");
    oidToNameMapping.put("2.16.840.1.114412.1.1", "DigiCertOVCert");
    oidToNameMapping.put("2.16.840.1.114412.1.11",
                         "DigiCertFederatedDeviceCert");
    oidToNameMapping.put("2.16.840.1.114412.1.2", "DigiCertDVCert");
    oidToNameMapping.put("2.16.840.1.114412.1.3.0.1", "DigiCertGlobalCAPolicy");
    oidToNameMapping.put("2.16.840.1.114412.1.3.0.2",
                         "DigiCertHighAssuranceEVCAPolicy");
    oidToNameMapping.put("2.16.840.1.114412.1.3.0.3",
                         "DigiCertGlobalRootCAPolicy");
    oidToNameMapping.put("2.16.840.1.114412.1.3.0.4",
                         "DigiCertAssuredIDRootCAPolicy");
    oidToNameMapping.put("2.16.840.1.114412.2.1", "DigiCert EV policy");
    oidToNameMapping.put("2.16.840.1.114412.2.2", "DigiCertEVCert");
    oidToNameMapping.put("2.16.840.1.114412.2.3", "DigiCertObjectSigningCert");
    oidToNameMapping.put("2.16.840.1.114412.2.3.1", "DigiCertCodeSigningCert");
    oidToNameMapping.put("2.16.840.1.114412.2.3.11",
                         "DigiCertKernelCodeSigningCert");
    oidToNameMapping.put("2.16.840.1.114412.2.3.2",
                         "DigiCertEVCodeSigningCert");
    oidToNameMapping.put("2.16.840.1.114412.2.3.21",
                         "DigiCertDocumentSigningCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4", "DigiCertClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.1.1",
                         "DigiCertLevel1PersonalClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.1.2",
                         "DigiCertLevel1EnterpriseClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.2", "DigiCertLevel2ClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.3.1",
                         "DigiCertLevel3USClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.3.2",
                         "DigiCertLevel3CBPClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.4.1",
                         "DigiCertLevel4USClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.4.2",
                         "DigiCertLevel4CBPClientCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.5.1",
                         "DigiCertPIVHardwareCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.5.2",
                         "DigiCertPIVCardAuthCert");
    oidToNameMapping.put("2.16.840.1.114412.2.4.5.3",
                         "DigiCertPIVContentSigningCert");
    oidToNameMapping.put("2.16.840.1.114412.31.4.31.1", "DigiCertGridHostCert");
    oidToNameMapping.put("2.16.840.1.114412.4.31", "DigiCertGridClassicCert");
    oidToNameMapping.put("2.16.840.1.114412.4.31.5",
                         "DigiCertGridIntegratedCert");
    oidToNameMapping.put("2.16.840.1.114413.1.7.23.3", "GoDaddy EV policy");
    oidToNameMapping.put("2.16.840.1.114414.1.7.23.3", "Starfield EV policy");
    oidToNameMapping.put("2.23.134.1.2.1.8.210",
                         "PostSignumCommercialServerPolicy");
    oidToNameMapping.put("2.23.134.1.2.2.3", "PostSignumPublicCA ");
    oidToNameMapping.put("2.23.134.1.4.2.1", "PostSignumRootQCA  ");
    oidToNameMapping.put("2.23.136.1.1.1", "MRTDSignatureData");
    oidToNameMapping.put("2.23.42.0", "ContentType");
    oidToNameMapping.put("2.23.42.0.0", "PanData");
    oidToNameMapping.put("2.23.42.0.1", "PanToken");
    oidToNameMapping.put("2.23.42.0.2", "PanOnly");
    oidToNameMapping.put("2.23.42.1", "MsgExt");
    oidToNameMapping.put("2.23.42.10", "National");
    oidToNameMapping.put("2.23.42.10.392", "Japan");
    oidToNameMapping.put("2.23.42.2", "Field");
    oidToNameMapping.put("2.23.42.2.0", "FullName");
    oidToNameMapping.put("2.23.42.2.1", "GivenName");
    oidToNameMapping.put("2.23.42.2.10", "Amount");
    oidToNameMapping.put("2.23.42.2.11", "AccountNumber");
    oidToNameMapping.put("2.23.42.2.12", "PassPhrase");
    oidToNameMapping.put("2.23.42.2.2", "FamilyName");
    oidToNameMapping.put("2.23.42.2.3", "BirthFamilyName");
    oidToNameMapping.put("2.23.42.2.4", "PlaceName");
    oidToNameMapping.put("2.23.42.2.5", "IdentificationNumber");
    oidToNameMapping.put("2.23.42.2.6", "Month");
    oidToNameMapping.put("2.23.42.2.7", "Date");
    oidToNameMapping.put("2.23.42.2.8", "Address");
    oidToNameMapping.put("2.23.42.2.9", "Telephone");
    oidToNameMapping.put("2.23.42.3", "Attribute");
    oidToNameMapping.put("2.23.42.3.0", "Cert");
    oidToNameMapping.put("2.23.42.3.0.0", "RootKeyThumb");
    oidToNameMapping.put("2.23.42.3.0.1", "AdditionalPolicy");
    oidToNameMapping.put("2.23.42.4", "Algorithm");
    oidToNameMapping.put("2.23.42.5", "Policy");
    oidToNameMapping.put("2.23.42.5.0", "Root");
    oidToNameMapping.put("2.23.42.6", "Module");
    oidToNameMapping.put("2.23.42.7", "CertExt");
    oidToNameMapping.put("2.23.42.7.0", "HashedRootKey");
    oidToNameMapping.put("2.23.42.7.1", "CertificateType");
    oidToNameMapping.put("2.23.42.7.2", "MerchantData");
    oidToNameMapping.put("2.23.42.7.3", "CardCertRequired");
    oidToNameMapping.put("2.23.42.7.4", "Tunneling");
    oidToNameMapping.put("2.23.42.7.5", "SetExtensions");
    oidToNameMapping.put("2.23.42.7.6", "SetQualifier");
    oidToNameMapping.put("2.23.42.8", "Brand");
    oidToNameMapping.put("2.23.42.8.1", "IATA-ATA");
    oidToNameMapping.put("2.23.42.8.30", "Diners");
    oidToNameMapping.put("2.23.42.8.34", "AmericanExpress");
    oidToNameMapping.put("2.23.42.8.4", "VISA");
    oidToNameMapping.put("2.23.42.8.5", "MasterCard");
    oidToNameMapping.put("2.23.42.8.6011", "Novus");
    oidToNameMapping.put("2.23.42.9", "Vendor");
    oidToNameMapping.put("2.23.42.9.0", "GlobeSet");
    oidToNameMapping.put("2.23.42.9.1", "IBM");
    oidToNameMapping.put("2.23.42.9.10", "Griffin");
    oidToNameMapping.put("2.23.42.9.11", "Certicom");
    oidToNameMapping.put("2.23.42.9.12", "OSS");
    oidToNameMapping.put("2.23.42.9.13", "TenthMountain");
    oidToNameMapping.put("2.23.42.9.14", "Antares");
    oidToNameMapping.put("2.23.42.9.15", "ECC");
    oidToNameMapping.put("2.23.42.9.16", "Maithean");
    oidToNameMapping.put("2.23.42.9.17", "Netscape");
    oidToNameMapping.put("2.23.42.9.18", "Verisign");
    oidToNameMapping.put("2.23.42.9.19", "BlueMoney");
    oidToNameMapping.put("2.23.42.9.2", "CyberCash");
    oidToNameMapping.put("2.23.42.9.20", "Lacerte");
    oidToNameMapping.put("2.23.42.9.21", "Fujitsu");
    oidToNameMapping.put("2.23.42.9.22", "ELab");
    oidToNameMapping.put("2.23.42.9.23", "Entrust");
    oidToNameMapping.put("2.23.42.9.24", "VIAnet");
    oidToNameMapping.put("2.23.42.9.25", "III");
    oidToNameMapping.put("2.23.42.9.26", "OpenMarket");
    oidToNameMapping.put("2.23.42.9.27", "Lexem");
    oidToNameMapping.put("2.23.42.9.28", "Intertrader");
    oidToNameMapping.put("2.23.42.9.29", "Persimmon");
    oidToNameMapping.put("2.23.42.9.3", "Terisa");
    oidToNameMapping.put("2.23.42.9.30", "NABLE");
    oidToNameMapping.put("2.23.42.9.31", "Espace-net");
    oidToNameMapping.put("2.23.42.9.32", "Hitachi");
    oidToNameMapping.put("2.23.42.9.33", "Microsoft");
    oidToNameMapping.put("2.23.42.9.34", "NEC");
    oidToNameMapping.put("2.23.42.9.35", "Mitsubishi");
    oidToNameMapping.put("2.23.42.9.36", "NCR");
    oidToNameMapping.put("2.23.42.9.37", "E-COMM");
    oidToNameMapping.put("2.23.42.9.38", "Gemplus");
    oidToNameMapping.put("2.23.42.9.4", "RSADSI");
    oidToNameMapping.put("2.23.42.9.5", "VeriFone");
    oidToNameMapping.put("2.23.42.9.6", "TrinTech");
    oidToNameMapping.put("2.23.42.9.7", "BankGate");
    oidToNameMapping.put("2.23.42.9.8", "GTE");
    oidToNameMapping.put("2.23.42.9.9", "CompuSource");
    oidToNameMapping.put("2.5.29.1", "AuthorityKeyIdentifier");
    oidToNameMapping.put("2.5.29.10", "BasicConstraints");
    oidToNameMapping.put("2.5.29.11", "NameConstraints");
    oidToNameMapping.put("2.5.29.12", "PolicyConstraints");
    oidToNameMapping.put("2.5.29.13", "BasicConstraints");
    oidToNameMapping.put("2.5.29.14", "SubjectKeyIdentifier");
    oidToNameMapping.put("2.5.29.15", "KeyUsage");
    oidToNameMapping.put("2.5.29.16", "PrivateKeyUsagePeriod");
    oidToNameMapping.put("2.5.29.17", "SubjectAltName");
    oidToNameMapping.put("2.5.29.18", "IssuerAltName");
    oidToNameMapping.put("2.5.29.19", "BasicConstraints");
    oidToNameMapping.put("2.5.29.2", "KeyAttributes");
    oidToNameMapping.put("2.5.29.20", "CRLNumber");
    oidToNameMapping.put("2.5.29.21", "CRLReason");
    oidToNameMapping.put("2.5.29.22", "ExpirationDate");
    oidToNameMapping.put("2.5.29.23", "InstructionCode");
    oidToNameMapping.put("2.5.29.24", "InvalidityDate");
    oidToNameMapping.put("2.5.29.25", "CRLDistributionPoints");
    oidToNameMapping.put("2.5.29.26", "IssuingDistributionPoint");
    oidToNameMapping.put("2.5.29.27", "DeltaCRLIndicator");
    oidToNameMapping.put("2.5.29.28", "IssuingDistributionPoint");
    oidToNameMapping.put("2.5.29.29", "CertificateIssuer");
    oidToNameMapping.put("2.5.29.3", "CertificatePolicies");
    oidToNameMapping.put("2.5.29.30", "NameConstraints");
    oidToNameMapping.put("2.5.29.31", "CRLDistributionPoints");
    oidToNameMapping.put("2.5.29.32", "CertificatePolicies");
    oidToNameMapping.put("2.5.29.32.0", "AnyPolicy");
    oidToNameMapping.put("2.5.29.33", "PolicyMappings");
    oidToNameMapping.put("2.5.29.34", "PolicyConstraints");
    oidToNameMapping.put("2.5.29.35", "AuthorityKeyIdentifier");
    oidToNameMapping.put("2.5.29.36", "PolicyConstraints");
    oidToNameMapping.put("2.5.29.37", "ExtKeyUsage");
    oidToNameMapping.put("2.5.29.37.0", "AnyExtendedKeyUsage");
    oidToNameMapping.put("2.5.29.38", "AuthorityAttributeIdentifier");
    oidToNameMapping.put("2.5.29.39", "RoleSpecCertIdentifier");
    oidToNameMapping.put("2.5.29.4", "KeyUsageRestriction");
    oidToNameMapping.put("2.5.29.40", "CRLStreamIdentifier");
    oidToNameMapping.put("2.5.29.41", "BasicAttConstraints");
    oidToNameMapping.put("2.5.29.42", "DelegatedNameConstraints");
    oidToNameMapping.put("2.5.29.43", "TimeSpecification");
    oidToNameMapping.put("2.5.29.44", "CRLScope");
    oidToNameMapping.put("2.5.29.45", "StatusReferrals");
    oidToNameMapping.put("2.5.29.46", "FreshestCRL");
    oidToNameMapping.put("2.5.29.47", "OrderedList");
    oidToNameMapping.put("2.5.29.48", "AttributeDescriptor");
    oidToNameMapping.put("2.5.29.49", "UserNotice");
    oidToNameMapping.put("2.5.29.5", "PolicyMapping");
    oidToNameMapping.put("2.5.29.50", "SOAIdentifier");
    oidToNameMapping.put("2.5.29.51", "BaseUpdateTime");
    oidToNameMapping.put("2.5.29.52", "AcceptableCertPolicies");
    oidToNameMapping.put("2.5.29.53", "DeltaInfo");
    oidToNameMapping.put("2.5.29.54", "InhibitAnyPolicy");
    oidToNameMapping.put("2.5.29.55", "TargetInformation");
    oidToNameMapping.put("2.5.29.56", "NoRevAvail");
    oidToNameMapping.put("2.5.29.57", "AcceptablePrivilegePolicies");
    oidToNameMapping.put("2.5.29.58", "ToBeRevoked");
    oidToNameMapping.put("2.5.29.59", "RevokedGroups");
    oidToNameMapping.put("2.5.29.6", "SubtreesConstraint");
    oidToNameMapping.put("2.5.29.60", "ExpiredCertsOnCRL");
    oidToNameMapping.put("2.5.29.61", "IndirectIssuer");
    oidToNameMapping.put("2.5.29.62", "NoAssertion");
    oidToNameMapping.put("2.5.29.63", "AAissuingDistributionPoint");
    oidToNameMapping.put("2.5.29.64", "IssuedOnBehalfOf");
    oidToNameMapping.put("2.5.29.65", "SingleUse");
    oidToNameMapping.put("2.5.29.66", "GroupAC");
    oidToNameMapping.put("2.5.29.67", "AllowedAttAss");
    oidToNameMapping.put("2.5.29.68", "AttributeMappings");
    oidToNameMapping.put("2.5.29.69", "HolderNameConstraints");
    oidToNameMapping.put("2.5.29.7", "SubjectAltName");
    oidToNameMapping.put("2.5.29.8", "IssuerAltName");
    oidToNameMapping.put("2.5.29.9", "SubjectDirectoryAttributes");
    oidToNameMapping.put("2.5.4.0", "ObjectClass");
    oidToNameMapping.put("2.5.4.1", "AliasedEntryName");
    oidToNameMapping.put("2.5.4.10", "OrganizationName");
    oidToNameMapping.put("2.5.4.10.1", "CollectiveOrganizationName");
    oidToNameMapping.put("2.5.4.11", "OrganizationalUnitName");
    oidToNameMapping.put("2.5.4.11.1", "CollectiveOrganizationalUnitName");
    oidToNameMapping.put("2.5.4.12", "Title");
    oidToNameMapping.put("2.5.4.13", "Description");
    oidToNameMapping.put("2.5.4.14", "SearchGuide");
    oidToNameMapping.put("2.5.4.15", "BusinessCategory");
    oidToNameMapping.put("2.5.4.16", "PostalAddress");
    oidToNameMapping.put("2.5.4.16.1", "CollectivePostalAddress");
    oidToNameMapping.put("2.5.4.17", "PostalCode");
    oidToNameMapping.put("2.5.4.17.1", "CollectivePostalCode");
    oidToNameMapping.put("2.5.4.18", "PostOfficeBox");
    oidToNameMapping.put("2.5.4.18.1", "CollectivePostOfficeBox");
    oidToNameMapping.put("2.5.4.19", "PhysicalDeliveryOfficeName");
    oidToNameMapping.put("2.5.4.19.1", "CollectivePhysicalDeliveryOfficeName");
    oidToNameMapping.put("2.5.4.2", "KnowledgeInformation");
    oidToNameMapping.put("2.5.4.20", "TelephoneNumber");
    oidToNameMapping.put("2.5.4.20.1", "CollectiveTelephoneNumber");
    oidToNameMapping.put("2.5.4.21", "TelexNumber");
    oidToNameMapping.put("2.5.4.21.1", "CollectiveTelexNumber");
    oidToNameMapping.put("2.5.4.22", "TeletexTerminalIdentifier");
    oidToNameMapping.put("2.5.4.22.1", "CollectiveTeletexTerminalIdentifier");
    oidToNameMapping.put("2.5.4.23", "FacsimileTelephoneNumber");
    oidToNameMapping.put("2.5.4.23.1", "CollectiveFacsimileTelephoneNumber");
    oidToNameMapping.put("2.5.4.24", "X121Address");
    oidToNameMapping.put("2.5.4.25", "InternationalISDNNumber");
    oidToNameMapping.put("2.5.4.25.1", "CollectiveInternationalISDNNumber");
    oidToNameMapping.put("2.5.4.26", "RegisteredAddress");
    oidToNameMapping.put("2.5.4.27", "DestinationIndicator");
    oidToNameMapping.put("2.5.4.28", "PreferredDeliveryMehtod");
    oidToNameMapping.put("2.5.4.29", "PresentationAddress");
    oidToNameMapping.put("2.5.4.3", "CommonName");
    oidToNameMapping.put("2.5.4.30", "SupportedApplicationContext");
    oidToNameMapping.put("2.5.4.31", "Member");
    oidToNameMapping.put("2.5.4.32", "Owner");
    oidToNameMapping.put("2.5.4.33", "RoleOccupant");
    oidToNameMapping.put("2.5.4.34", "SeeAlso");
    oidToNameMapping.put("2.5.4.35", "UserPassword");
    oidToNameMapping.put("2.5.4.36", "UserCertificate");
    oidToNameMapping.put("2.5.4.37", "CaCertificate");
    oidToNameMapping.put("2.5.4.38", "AuthorityRevocationList");
    oidToNameMapping.put("2.5.4.39", "CertificateRevocationList");
    oidToNameMapping.put("2.5.4.4", "Surname");
    oidToNameMapping.put("2.5.4.40", "CrossCertificatePair");
    oidToNameMapping.put("2.5.4.41", "Name");
    oidToNameMapping.put("2.5.4.42", "GivenName");
    oidToNameMapping.put("2.5.4.43", "Initials");
    oidToNameMapping.put("2.5.4.44", "GenerationQualifier");
    oidToNameMapping.put("2.5.4.45", "UniqueIdentifier");
    oidToNameMapping.put("2.5.4.46", "DnQualifier");
    oidToNameMapping.put("2.5.4.47", "EnhancedSearchGuide");
    oidToNameMapping.put("2.5.4.48", "ProtocolInformation");
    oidToNameMapping.put("2.5.4.49", "DistinguishedName");
    oidToNameMapping.put("2.5.4.5", "SerialNumber");
    oidToNameMapping.put("2.5.4.50", "UniqueMember");
    oidToNameMapping.put("2.5.4.51", "HouseIdentifier");
    oidToNameMapping.put("2.5.4.52", "SupportedAlgorithms");
    oidToNameMapping.put("2.5.4.53", "DeltaRevocationList");
    oidToNameMapping.put("2.5.4.54", "DmdName");
    oidToNameMapping.put("2.5.4.55", "Clearance");
    oidToNameMapping.put("2.5.4.56", "DefaultDirQop");
    oidToNameMapping.put("2.5.4.57", "AttributeIntegrityInfo");
    oidToNameMapping.put("2.5.4.58", "AttributeCertificate");
    oidToNameMapping.put("2.5.4.59", "AttributeCertificateRevocationList");
    oidToNameMapping.put("2.5.4.6", "CountryName");
    oidToNameMapping.put("2.5.4.60", "ConfKeyInfo");
    oidToNameMapping.put("2.5.4.61", "AACertificate");
    oidToNameMapping.put("2.5.4.62", "AttributeDescriptorCertificate");
    oidToNameMapping.put("2.5.4.63", "AttributeAuthorityRevocationList");
    oidToNameMapping.put("2.5.4.64", "FamilyInformation");
    oidToNameMapping.put("2.5.4.65", "Pseudonym");
    oidToNameMapping.put("2.5.4.66", "CommunicationsService");
    oidToNameMapping.put("2.5.4.67", "CommunicationsNetwork");
    oidToNameMapping.put("2.5.4.68", "CertificationPracticeStmt");
    oidToNameMapping.put("2.5.4.69", "CertificatePolicy");
    oidToNameMapping.put("2.5.4.7", "LocalityName");
    oidToNameMapping.put("2.5.4.7.1", "CollectiveLocalityName");
    oidToNameMapping.put("2.5.4.70", "PkiPath");
    oidToNameMapping.put("2.5.4.71", "PrivPolicy");
    oidToNameMapping.put("2.5.4.72", "Role");
    oidToNameMapping.put("2.5.4.73", "DelegationPath");
    oidToNameMapping.put("2.5.4.74", "ProtPrivPolicy");
    oidToNameMapping.put("2.5.4.75", "XMLPrivilegeInfo");
    oidToNameMapping.put("2.5.4.76", "XmlPrivPolicy");
    oidToNameMapping.put("2.5.4.77", "Uuidpair");
    oidToNameMapping.put("2.5.4.78", "TagOid");
    oidToNameMapping.put("2.5.4.79", "UiiFormat");
    oidToNameMapping.put("2.5.4.8", "StateOrProvinceName");
    oidToNameMapping.put("2.5.4.8.1", "CollectiveStateOrProvinceName");
    oidToNameMapping.put("2.5.4.80", "UiiInUrh");
    oidToNameMapping.put("2.5.4.81", "ContentUrl");
    oidToNameMapping.put("2.5.4.82", "Permission");
    oidToNameMapping.put("2.5.4.83", "Uri");
    oidToNameMapping.put("2.5.4.84", "PwdAttribute");
    oidToNameMapping.put("2.5.4.85", "UserPwd");
    oidToNameMapping.put("2.5.4.86", "Urn");
    oidToNameMapping.put("2.5.4.87", "Url");
    oidToNameMapping.put("2.5.4.88", "UtmCoordinates");
    oidToNameMapping.put("2.5.4.89", "UrnC");
    oidToNameMapping.put("2.5.4.90", "Uii");
    oidToNameMapping.put("2.5.4.91", "Epc");
    oidToNameMapping.put("2.5.4.92", "TagAfi");
    oidToNameMapping.put("2.5.4.93", "EpcFormat");
    oidToNameMapping.put("2.5.4.94", "EpcInUrn");
    oidToNameMapping.put("2.5.4.95", "LdapUrl");
    oidToNameMapping.put("2.5.4.96", "LdapUrl");
    oidToNameMapping.put("2.5.4.97", "OrganizationIdentifier");
    oidToNameMapping.put("2.5.4.9", "StreetAddress");
    oidToNameMapping.put("2.5.4.9.1", "CollectiveStreetAddress");
    oidToNameMapping.put("2.5.6.0", "Top");
    oidToNameMapping.put("2.5.6.1", "Alias");
    oidToNameMapping.put("2.5.6.10", "ResidentialPerson");
    oidToNameMapping.put("2.5.6.11", "ApplicationProcess");
    oidToNameMapping.put("2.5.6.12", "ApplicationEntity");
    oidToNameMapping.put("2.5.6.13", "DSA");
    oidToNameMapping.put("2.5.6.14", "Device");
    oidToNameMapping.put("2.5.6.15", "StrongAuthenticationUser");
    oidToNameMapping.put("2.5.6.16", "CertificateAuthority");
    oidToNameMapping.put("2.5.6.17", "GroupOfUniqueNames");
    oidToNameMapping.put("2.5.6.2", "Country");
    oidToNameMapping.put("2.5.6.21", "PkiUser");
    oidToNameMapping.put("2.5.6.22", "PkiCA");
    oidToNameMapping.put("2.5.6.3", "Locality");
    oidToNameMapping.put("2.5.6.4", "Organization");
    oidToNameMapping.put("2.5.6.5", "OrganizationalUnit");
    oidToNameMapping.put("2.5.6.6", "Person");
    oidToNameMapping.put("2.5.6.7", "OrganizationalPerson");
    oidToNameMapping.put("2.5.6.8", "OrganizationalRole");
    oidToNameMapping.put("2.5.6.9", "GroupOfNames");
    oidToNameMapping.put("2.5.8.1.1", "Rsa");
    oidToNameMapping.put("2.54.1775.2", "HashedRootKey");
    oidToNameMapping.put("2.54.1775.3", "CertificateType");
    oidToNameMapping.put("2.54.1775.4", "MerchantData");
    oidToNameMapping.put("2.54.1775.5", "CardCertRequired");
    oidToNameMapping.put("2.54.1775.6", "Tunneling");
    oidToNameMapping.put("2.54.1775.7", "SetQualifier");
    oidToNameMapping.put("2.54.1775.99", "SetData");
  }

  /**
   * Get subset of OIDs that start with the given prefix.
   *
   * @param prefix First n elements of the OID
   * @return All OIDs that match the given prefix
   */
  public static ObjectOid[] getAllOidsStartingWith(String prefix) {
    ObjectOid[] oids =
        oidToNameMapping.entrySet()
            .stream()
            .filter(e -> e.getKey().startsWith(prefix))
            .map(e -> new ObjectOid(prefix, e.getKey(), e.getValue()))
            .sorted()
            .collect(Collectors.toList())
            .toArray(new ObjectOid[0]);
    return oids;
  }

  /**
   * Resolve OID to human readable name.
   *
   * @param oid OID as string
   * @return Friendly name or null if unknown
   */
  public static String getFriendlyName(String oid) {
    return oidToNameMapping.get(oid);
  }

  /**
   * Get string representation of Object Identifier.
   *
   * @param objectIdentifer Object Identifier
   * @return String representation of Object Identifier
   */
  public static String toString(ASN1ObjectIdentifier objectIdentifer) {
    String id = objectIdentifer.getId();
    String name = oidToNameMapping.get(id);

    if (name == null) {
      return id;
    }

    return MessageFormat.format("{0} ({1})", name, id);
  }
}
