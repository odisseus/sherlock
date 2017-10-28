package model

/**
  * WOJ;POW;GMI;RODZ_GMI;SYM;SYM_UL;CECHA;NAZWA_1;NAZWA_2;STAN_NA
  */
case class TerytStreet(
  voivodeship: Int,
  county: Int,
  municipality: Int ,
  municipalityType: Int,
  placeId: Int,
  streetId: Int,
  streetType: String,
  streetNameMain: String,
  streetNameAdditional: String,
  updatedAt: String,
)
