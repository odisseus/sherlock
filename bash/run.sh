#!/bin/bash 

function fix {
  while read line; do
    # urln=`echo $line | awk -F\; '{gsub(/ /,"+",$2); print "http://nominatim.openstreetmap.org/?format=json&city=krakow&country=poland&limit=1&street="$2"+"$3}'`
    urlg=`echo $line | awk -F\; '{gsub(/ /,"+",$2); print "https://maps.googleapis.com/maps/api/geocode/json?address="$2"+"$3",+Krakow,+Poland&key=AIzaSyBAt8vznwzPUL4VNBswvrEI9z3G9w3vzFM"}'`
  #  longlat=`curl $urln | jq .[0].boundingbox[2,0] | xargs echo`
    longlat=`curl $urlg | jq .results[0].geometry.location.lng,.results[0].geometry.location.lat | xargs echo`
    url2=`echo $longlat | awk '{print "http://epsg.io/trans?s_srs=4326&t_srs=2178&x="$1"&y="$2}'`
    xy=`curl $url2 | jq .x,.y | xargs echo | sed 's/ /;/g'`
    echo $line";"$xy | tr -d '\r'
  done
}

function clean {
  awk -F\; '{\
  gsub(/ą/,"A",$1);\
  gsub(/ä/,"A",$1);\
  gsub(/ć/,"C",$1);\
  gsub(/ę/,"E",$1);\
  gsub(/ł/,"L",$1);\
  gsub(/ń/,"N",$1);\
  gsub(/ó/,"O",$1);\
  gsub(/ö/,"O",$1);\
  gsub(/ś/,"S",$1);\
  gsub(/ü/,"U",$1);\
  gsub(/ź/,"Z",$1);\
  gsub(/ż/,"Z",$1);\
  gsub(/Ą/,"A",$1);\
  gsub(/Ä/,"A",$1);\
  gsub(/Ć/,"C",$1);\
  gsub(/Ę/,"E",$1);\
  gsub(/Ł/,"L",$1);\
  gsub(/Ń/,"N",$1);\
  gsub(/Ó/,"O",$1);\
  gsub(/Ö/,"O",$1);\
  gsub(/Ś/,"S",$1);\
  gsub(/Ü/,"U",$1);\
  gsub(/Ź/,"Z",$1);\
  gsub(/Ż/,"Z",$1);\
  gsub(/AL[\. ]/,"",$1);\
  gsub(/ALEJA /,"",$1);\
  gsub(/UL[\. ]/,"",$1);\
  gsub(/ULICA /,"",$1);\
  gsub(/PL[\. ]/,"",$1);\
  gsub(/PLAC /,"",$1);\
  gsub(/OS[\. ]/,"",$1);\
  gsub(/OSIEDLE /,"",$1);\
  gsub(/RYNEK /,"",$1);\
  gsub(/GENERALA /,"",$1);\
  gsub(/KS\. BP\./,"",$1);\
  gsub(/KS[\. ]/,"",$1);\
  gsub(/KSIEDZA/,"",$1);\
  gsub(/BP[\. ]/,"",$1);\
  gsub(/BPA[\. ]/,"",$1);\
  gsub(/ABP[\. ]/,"",$1);\
  gsub(/ARCYBISKUPA /,"",$1);\
  gsub(/MARSZ[\. ]/,"",$1);\
  gsub(/MARSZALKA /,"",$1);\
  gsub(/POR[\. ]/,"",$1);\
  gsub(/PORUCZNIKA /,"",$1);\
  gsub(/PROF[\. ]/,"",$1);\
  gsub(/PROFESORA /,"",$1);\
  gsub(/PROFESOR /,"",$1);\
  gsub(/DR[\. ]/,"",$1);\
  gsub(/DOKTORA /,"",$1);\
  gsub(/PLK[\. ]/,"",$1);\
  gsub(/PULKOWNIKA /,"",$1);\
  gsub(/RTM[\. ]/,"",$1);\
  gsub(/ROTMISTRZA /,"",$1);\
  gsub(/SW[\. ]/,"",$1);\
  gsub(/SWIETEGO /,"",$1);\
  gsub(/SWIETEJ /,"",$1);\
  gsub(/^.[\.  ]/,"",$1);\
  gsub(/^..\./,"",$1);\
  gsub(/[ \"\.\<\>\-]/,"",$1);\
  OFS=";";print $0}' \
  | sort -k1,1 -t\;
}

function run {
  echo Starting time:
  date

  echo Preparing dane
  tail -n +2 "dane wejściowe.csv" \
  | awk -F\; '{print toupper($2" "$3)";"$0}' \
  | clean > dane.txt

  echo Preparing slownik
  tail -n +2 "Słownik Adresów.csv" \
  | awk -F\; '{OFS=";";print toupper($1" "$2),$11,$12}' \
  | while read line; do
      while [[ ! $line =~ ^[^\ ]*\;.* ]]; do
        echo $line
        line=${line#* }
      done
    done \
  | clean > slownik.txt

  echo Joining pairable
  join -t";" -1 1 -2 1 dane.txt slownik.txt \
  | tr -d '\r' \
  | cut -f 2- -d\; \
  > pairable.txt

  echo Joining unpairable
  join -t";" -1 1 -2 1 -v 1 dane.txt slownik.txt \
  | cut -f 2- -d\; \
  > impairable.txt

  fix <impairable.txt >fix.txt

  cat pairable.txt fix.txt | sort -k1 -t\; -n | grep ';0;$' > unmatched.txt
  cat pairable.txt fix.txt | sort -k1 -t\; -n | grep -v ';0;$' > matched.txt

  echo Ending time
  date

  wc -l pairable.txt
  wc -l impairable.txt
  wc -l matched.txt
  wc -l unmatched.txt
}

time run
