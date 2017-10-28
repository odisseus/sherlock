grep -v "ADR_NR" "Słownik Adresów.csv" \
| awk -F\; '{OFS=";";print toupper($1" "$2),$11,$12}' \
> slownik.txt

while read line; do
  while [[ ! $line =~ ^[^\ ]*\;.* ]]; do
    echo $line
    line=${line#* }
  done
done <slownik.txt >slownik3.txt

./clean.sh slownik3.txt > slownik2.txt
