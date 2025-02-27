/*
 * MIT License
 *
 * Copyright (c) 2025 LabyMedia GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.labymod.serverapi.core.model.display;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public class TabListFlag {

  private final UUID uniqueId;
  private final TabListFlagCountryCode countryCode;

  private TabListFlag(@NotNull UUID uniqueId, @NotNull TabListFlagCountryCode countryCode) {
    Objects.requireNonNull(uniqueId, "Unique id cannot be null");
    Objects.requireNonNull(countryCode, "Country code cannot be null");
    this.uniqueId = uniqueId;
    this.countryCode = countryCode;
  }

  private TabListFlag(@NotNull UUID uniqueId, @NotNull String countryCode) {
    Objects.requireNonNull(uniqueId, "Unique id cannot be null");
    Objects.requireNonNull(countryCode, "Country code cannot be null");
    this.uniqueId = uniqueId;
    this.countryCode = TabListFlagCountryCode.getCountryCode(countryCode);
    Objects.requireNonNull(this.countryCode, "No supported country code found for " + countryCode);
  }

  public static TabListFlag create(
      @NotNull UUID uniqueId,
      @NotNull TabListFlagCountryCode countryCode
  ) {
    return new TabListFlag(uniqueId, countryCode);
  }

  public static TabListFlag create(@NotNull UUID uniqueId, @NotNull String countryCode) {
    return new TabListFlag(uniqueId, countryCode);
  }

  public UUID getUniqueId() {
    return this.uniqueId;
  }

  public TabListFlagCountryCode getCountryCode() {
    return this.countryCode;
  }

  @Override
  public String toString() {
    return "TabListFlag{" +
        "uniqueId=" + this.uniqueId +
        ", countryCode=" + this.countryCode +
        '}';
  }

  public enum TabListFlagCountryCode {
    AD,
    AE,
    AF,
    AG,
    AI,
    AL,
    AM,
    AO,
    AQ,
    AR,
    AS,
    AT,
    AU,
    AW,
    AX,
    AZ,

    BA,
    BB,
    BD,
    BE,
    BF,
    BG,
    BH,
    BI,
    BJ,
    BL,
    BM,
    BN,
    BO,
    BQ,
    BR,
    BS,
    BT,
    BV,
    BW,
    BY,
    BZ,

    CA,
    CC,
    CD,
    CF,
    CG,
    CH,
    CI,
    CK,
    CL,
    CM,
    CN,
    CO,
    CR,
    CU,
    CV,
    CW,
    CX,
    CY,
    CZ,

    DE,
    DJ,
    DK,
    DM,
    DO,
    DZ,

    EC,
    EE,
    EG,
    EH,
    ER,
    ES,
    ET,
    EU,

    FI,
    FJ,
    FK,
    FM,
    FO,
    FR,

    GA,
    GD_NG,
    GD_IR,
    GD_CT,
    GD_LS,
    GB,
    GD,
    GE,
    GF,
    GG,
    GH,
    GI,
    GL,
    GM,
    GN,
    GP,
    GQ,
    GR,
    GS,
    GT,
    GU,
    GW,
    GY,

    HK,
    HM,
    HN,
    HR,
    HT,
    HU,

    ID,
    IE,
    IL,
    IM,
    IN,
    IO,
    IQ,
    IR,
    IS,
    IT,

    JE,
    JM,
    JO,
    JP,

    KE,
    KG,
    KH,
    KI,
    KM,
    KN,
    KP,
    KR,
    KW,
    KY,
    KZ,

    LA,
    LB,
    LC,
    LI,
    LK,
    LR,
    LS,
    LT,
    LU,
    LV,
    LY,

    MA,
    MC,
    MD,
    ME,
    MF,
    MG,
    MH,
    MK,
    ML,
    MM,
    MN,
    MO,
    MP,
    MQ,
    MR,
    MS,
    MT,
    MU,
    MV,
    MW,
    MX,
    MY,
    MZ,

    NA,
    NC,
    NE,
    NF,
    NG,
    NI,
    NL,
    NO,
    NP,
    NR,
    NU,
    NZ,

    OM,

    PA,
    PE,
    PF,
    PG,
    PH,
    PK,
    PL,
    PM,
    PN,
    PR,
    PS,
    PT,
    PW,
    PY,

    QA,

    RE,
    RO,
    RS,
    RU,
    RW,

    SA,
    SB,
    SC,
    SD,
    SE,
    SG,
    SH,
    SI,
    SJ,
    SK,
    SL,
    SM,
    SN,
    SO,
    SR,
    SS,
    ST,
    SV,
    SX,
    SY,
    SZ,

    TC,
    TD,
    TF,
    TG,
    TH,
    TJ,
    TK,
    TL,
    TM,
    TN,
    TO,
    TR,
    TT,
    TV,
    TW,
    TZ,

    UA,
    UG,
    UM,
    US,
    UY,
    UZ,

    VA,
    VC,
    VE,
    VG,
    VI,
    VN,
    VU,

    WF,
    WS,

    XK,

    YE,
    YT,

    ZA,
    ZM,
    ZW,
    ;

    private static final TabListFlagCountryCode[] VALUES = values();

    public static TabListFlagCountryCode getCountryCode(String countryCode) {
      for (TabListFlagCountryCode value : VALUES) {
        if (value.name().equalsIgnoreCase(countryCode)) {
          return value;
        }
      }

      return null;
    }
  }
}