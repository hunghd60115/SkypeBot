package bittrex.entity.enums;

import bittrex.entity.deserializer.ExchangeDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Created by Onsiter on 2017/12/19.
 */
@JsonDeserialize(using = ExchangeDeserializer.class)
public enum Exchange {
    BTC_1ST("BTC-1ST"),
    BTC_2GIVE("BTC-2GIVE"),
    BTC_ABY("BTC-ABY"),
    BTC_ADA("BTC-ADA"),
    BTC_ADT("BTC-ADT"),
    BTC_ADX("BTC-ADX"),
    BTC_AEON("BTC-AEON"),
    BTC_AGRS("BTC-AGRS"),
    BTC_AMP("BTC-AMP"),
    BTC_ANT("BTC-ANT"),
    BTC_APX("BTC-APX"),
    BTC_ARDR("BTC-ARDR"),
    BTC_ARK("BTC-ARK"),
    BTC_AUR("BTC-AUR"),
    BTC_BAT("BTC-BAT"),
    BTC_BAY("BTC-BAY"),
    BTC_BCC("BTC-BCC"),
    BTC_BCY("BTC-BCY"),
    BTC_BITB("BTC-BITB"),
    BTC_BLITZ("BTC-BLITZ"),
    BTC_BLK("BTC-BLK"),
    BTC_BLOCK("BTC-BLOCK"),
    BTC_BNT("BTC-BNT"),
    BTC_BRK("BTC-BRK"),
    BTC_BRX("BTC-BRX"),
    BTC_BSD("BTC-BSD"),
    BTC_BTCD("BTC-BTCD"),
    BTC_BTG("BTC-BTG"),
    BTC_BURST("BTC-BURST"),
    BTC_BYC("BTC-BYC"),
    BTC_CANN("BTC-CANN"),
    BTC_CFI("BTC-CFI"),
    BTC_CLAM("BTC-CLAM"),
    BTC_CLOAK("BTC-CLOAK"),
    BTC_CLUB("BTC-CLUB"),
    BTC_COVAL("BTC-COVAL"),
    BTC_CPC("BTC-CPC"),
    BTC_CRB("BTC-CRB"),
    BTC_CRW("BTC-CRW"),
    BTC_CURE("BTC-CURE"),
    BTC_CVC("BTC-CVC"),
    BTC_DASH("BTC-DASH"),
    BTC_DCR("BTC-DCR"),
    BTC_DCT("BTC-DCT"),
    BTC_DGB("BTC-DGB"),
    BTC_DGD("BTC-DGD"),
    BTC_DMD("BTC-DMD"),
    BTC_DNT("BTC-DNT"),
    BTC_DOGE("BTC-DOGE"),
    BTC_DOPE("BTC-DOPE"),
    BTC_DTB("BTC-DTB"),
    BTC_DYN("BTC-DYN"),
    BTC_EBST("BTC-EBST"),
    BTC_EDG("BTC-EDG"),
    BTC_EFL("BTC-EFL"),
    BTC_EGC("BTC-EGC"),
    BTC_EMC("BTC-EMC"),
    BTC_EMC2("BTC-EMC2"),
    BTC_ENG("BTC-ENG"),
    BTC_ENRG("BTC-ENRG"),
    BTC_ERC("BTC-ERC"),
    BTC_ETC("BTC-ETC"),
    BTC_ETH("BTC-ETH"),
    BTC_EXCL("BTC-EXCL"),
    BTC_EXP("BTC-EXP"),
    BTC_FAIR("BTC-FAIR"),
    BTC_FCT("BTC-FCT"),
    BTC_FLDC("BTC-FLDC"),
    BTC_FLO("BTC-FLO"),
    BTC_FTC("BTC-FTC"),
    BTC_FUN("BTC-FUN"),
    BTC_GAM("BTC-GAM"),
    BTC_GAME("BTC-GAME"),
    BTC_GBG("BTC-GBG"),
    BTC_GBYTE("BTC-GBYTE"),
    BTC_GCR("BTC-GCR"),
    BTC_GEO("BTC-GEO"),
    BTC_GLD("BTC-GLD"),
    BTC_GNO("BTC-GNO"),
    BTC_GNT("BTC-GNT"),
    BTC_GOLOS("BTC-GOLOS"),
    BTC_GRC("BTC-GRC"),
    BTC_GRS("BTC-GRS"),
    BTC_GUP("BTC-GUP"),
    BTC_HMQ("BTC-HMQ"),
    BTC_INCNT("BTC-INCNT"),
    BTC_INFX("BTC-INFX"),
    BTC_IOC("BTC-IOC"),
    BTC_ION("BTC-ION"),
    BTC_IOP("BTC-IOP"),
    BTC_KMD("BTC-KMD"),
    BTC_KORE("BTC-KORE"),
    BTC_LBC("BTC-LBC"),
    BTC_LGD("BTC-LGD"),
    BTC_LMC("BTC-LMC"),
    BTC_LSK("BTC-LSK"),
    BTC_LTC("BTC-LTC"),
    BTC_LUN("BTC-LUN"),
    BTC_MAID("BTC-MAID"),
    BTC_MANA("BTC-MANA"),
    BTC_MCO("BTC-MCO"),
    BTC_MEME("BTC-MEME"),
    BTC_MER("BTC-MER"),
    BTC_MLN("BTC-MLN"),
    BTC_MONA("BTC-MONA"),
    BTC_MTL("BTC-MTL"),
    BTC_MUE("BTC-MUE"),
    BTC_MUSIC("BTC-MUSIC"),
    BTC_MYST("BTC-MYST"),
    BTC_NAV("BTC-NAV"),
    BTC_NBT("BTC-NBT"),
    BTC_NEO("BTC-NEO"),
    BTC_NEOS("BTC-NEOS"),
    BTC_NLG("BTC-NLG"),
    BTC_NMR("BTC-NMR"),
    BTC_NXC("BTC-NXC"),
    BTC_NXS("BTC-NXS"),
    BTC_NXT("BTC-NXT"),
    BTC_OK("BTC-OK"),
    BTC_OMG("BTC-OMG"),
    BTC_OMNI("BTC-OMNI"),
    BTC_PART("BTC-PART"),
    BTC_PAY("BTC-PAY"),
    BTC_PDC("BTC-PDC"),
    BTC_PINK("BTC-PINK"),
    BTC_PIVX("BTC-PIVX"),
    BTC_PKB("BTC-PKB"),
    BTC_POT("BTC-POT"),
    BTC_POWR("BTC-POWR"),
    BTC_PPC("BTC-PPC"),
    BTC_PTC("BTC-PTC"),
    BTC_PTOY("BTC-PTOY"),
    BTC_QRL("BTC-QRL"),
    BTC_QTUM("BTC-QTUM"),
    BTC_QWARK("BTC-QWARK"),
    BTC_RADS("BTC-RADS"),
    BTC_RBY("BTC-RBY"),
    BTC_RCN("BTC-RCN"),
    BTC_RDD("BTC-RDD"),
    BTC_REP("BTC-REP"),
    BTC_RISE("BTC-RISE"),
    BTC_RLC("BTC-RLC"),
    BTC_SALT("BTC-SALT"),
    BTC_SBD("BTC-SBD"),
    BTC_SC("BTC-SC"),
    BTC_SEQ("BTC-SEQ"),
    BTC_SHIFT("BTC-SHIFT"),
    BTC_SIB("BTC-SIB"),
    BTC_SLR("BTC-SLR"),
    BTC_SLS("BTC-SLS"),
    BTC_SNGLS("BTC-SNGLS"),
    BTC_SNRG("BTC-SNRG"),
    BTC_SNT("BTC-SNT"),
    BTC_SPHR("BTC-SPHR"),
    BTC_SPR("BTC-SPR"),
    BTC_START("BTC-START"),
    BTC_STEEM("BTC-STEEM"),
    BTC_STORJ("BTC-STORJ"),
    BTC_STRAT("BTC-STRAT"),
    BTC_SWIFT("BTC-SWIFT"),
    BTC_SWT("BTC-SWT"),
    BTC_SYNX("BTC-SYNX"),
    BTC_SYS("BTC-SYS"),
    BTC_THC("BTC-THC"),
    BTC_TIX("BTC-TIX"),
    BTC_TKS("BTC-TKS"),
    BTC_TRIG("BTC-TRIG"),
    BTC_TRST("BTC-TRST"),
    BTC_TRUST("BTC-TRUST"),
    BTC_TX("BTC-TX"),
    BTC_UBQ("BTC-UBQ"),
    BTC_UNB("BTC-UNB"),
    BTC_VIA("BTC-VIA"),
    BTC_VIB("BTC-VIB"),
    BTC_VOX("BTC-VOX"),
    BTC_VRC("BTC-VRC"),
    BTC_VRM("BTC-VRM"),
    BTC_VTC("BTC-VTC"),
    BTC_VTR("BTC-VTR"),
    BTC_WAVES("BTC-WAVES"),
    BTC_WINGS("BTC-WINGS"),
    BTC_XAUR("BTC-XAUR"),
    BTC_XCP("BTC-XCP"),
    BTC_XDN("BTC-XDN"),
    BTC_XEL("BTC-XEL"),
    BTC_XEM("BTC-XEM"),
    BTC_XLM("BTC-XLM"),
    BTC_XMG("BTC-XMG"),
    BTC_XMR("BTC-XMR"),
    BTC_XMY("BTC-XMY"),
    BTC_XRP("BTC-XRP"),
    BTC_XST("BTC-XST"),
    BTC_XVC("BTC-XVC"),
    BTC_XVG("BTC-XVG"),
    BTC_XWC("BTC-XWC"),
    BTC_XZC("BTC-XZC"),
    BTC_ZCL("BTC-ZCL"),
    BTC_ZEC("BTC-ZEC"),
    BTC_ZEN("BTC-ZEN"),
    USDT_BTC("USDT-BTC"),
    BTC_SAFEX("BTC-SAFEX");

    private final String exchange;

    Exchange(final String exchange) {
        this.exchange = exchange;
    }

    public String getCode() {
        if (this.exchange == null) {
            return "null";
        }
        return this.exchange;
    }
}
