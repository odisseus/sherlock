cat $1 \
| tr "ąćęłńóśüźżĄĆĘŁŃÓŚÜŹŻ" "ACELNOSUZZACELNOSUZZ" \
| awk -F\; '{'\
'gsub(/AL[\. ]/,"",$1);'\
'gsub(/ALEJA /,"",$1);'\
'gsub(/UL[\. ]/,"",$1);'\
'gsub(/ULICA /,"",$1);'\
'gsub(/PL[\. ]/,"",$1);'\
'gsub(/PLAC /,"",$1);'\
'gsub(/OS[\. ]/,"",$1);'\
'gsub(/OSIEDLE /,"",$1);'\
'gsub(/RYNEK /,"",$1);'\
\
'gsub(/GEN[\. ]/,"",$1);'\
'gsub(/GENERALA /,"",$1);'\
'gsub(/KS\. BP\./,"",$1);'\
'gsub(/KS[\. ]/,"",$1);'\
'gsub(/KSIEDZA/,"",$1);'\
'gsub(/BP[\. ]/,"",$1);'\
'gsub(/BPA[\. ]/,"",$1);'\
'gsub(/ABP[\. ]/,"",$1);'\
'gsub(/ARCYBISKUPA /,"",$1);'\
'gsub(/MARSZ[\. ]/,"",$1);'\
'gsub(/MARSZALKA /,"",$1);'\
'gsub(/POR[\. ]/,"",$1);'\
'gsub(/PORUCZNIKA /,"",$1);'\
'gsub(/PROF[\. ]/,"",$1);'\
'gsub(/PROFESORA /,"",$1);'\
'gsub(/PROFESOR /,"",$1);'\
'gsub(/DR[\. ]/,"",$1);'\
'gsub(/DOKTORA /,"",$1);'\
'gsub(/PLK[\. ]/,"",$1);'\
'gsub(/PULKOWNIKA /,"",$1);'\
'gsub(/RTM[\. ]/,"",$1);'\
'gsub(/ROTMISTRZA /,"",$1);'\
'gsub(/SW[\. ]/,"",$1);'\
'gsub(/SWIETEGO /,"",$1);'\
'gsub(/SWIETEJ /,"",$1);'\
\
'gsub(/^.[\.  ]/,"",$1);'\
'gsub(/^..\./,"",$1);'\
'gsub(/[ \"\.\<\>\-]/,"",$1);'\
'OFS=";";print $0}' \
| sort -k1,1 -t\;
