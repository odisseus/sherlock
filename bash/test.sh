echo Starting time:
date
echo Preparing dane
tail -n +2 "dane wejściowe.csv" \
| awk -F\; '{print toupper($2" "$3)";"$0}' \
| ./clean.sh  > dane.txt

echo Preparing slownik
tail -n +2 "Słownik Adresów.csv" \
| awk -F\; '{OFS=";";print toupper($1" "$2),$11,$12}' \
| while read line; do
    while [[ ! $line =~ ^[^\ ]*\;.* ]]; do
      echo $line
      line=${line#* }
    done
  done \
| ./clean.sh > slownik.txt

echo Joining pairable
join -t";" -1 1 -2 1 dane.txt slownik.txt \
| tr -d '\r' \
| cut -f 2- -d\; \
> pairable.txt

echo Joining unpairable
join -t";" -1 1 -2 1 -v 1 dane.txt slownik.txt \
| cut -f 2- -d\; \
> impairable.txt

./fix.sh <impairable.txt >fix.txt

cat pairable.txt fix.txt | sort -k1 -t\; -n > final.txt

echo Ending time
date

wc -l pairable.txt
wc -l impairable.txt
wc -l fix.txt
wc -l final.txt
