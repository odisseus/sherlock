grep -v "ADR_NR" "Słownik Adresów.csv" \
| awk -F\; '{OFS=";";print toupper($1" "$2),$11,$12}' \
> slownik.txt

./clean.sh slownik.txt > slownik2.txt
