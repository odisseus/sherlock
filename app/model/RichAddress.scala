package model

/**
  * ULICA;ADR_NR;NAZWA_PRZEDROSTEK_A_CZESC;NAZWA_PRZEDROSTEK_B_CZESC;NAZWA_NAZWA_A_CZESC;NAZWA_GLOWNA_CZESC;
  * TYP_CIAGU_KOMUNIKACYJNEGO;TERYT_KOD;DZL_NUMER;DZL_NAZWA;GEOM_X;GEOM_Y
  */
case class RichAddress(
  street: String,
  number: String,
  namePrefixA: String,
  namePrefixB: String,
  nameAdditional: String,
  nameMain: String,
  thoroughfareType: String,
  territoryCode: Int,
  boroughCode: Int,
  boroughName: String,
  geomX: Double,
  geomY: Double
)
