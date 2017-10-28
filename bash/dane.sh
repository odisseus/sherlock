grep -v "stan_nawie" "dane wejściowe.csv" \
| awk -F\; '{print toupper($2" "$3)";"$0}' \
> dane.txt

./clean.sh dane.txt > dane2.txt
