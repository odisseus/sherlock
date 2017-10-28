wc -l impairable.txt
awk -F";" '{print $1"\t\t\t"$3"\t"$4}' impairable.txt | cut -f 1 > patterns
while read line; do grep --color $line slownik2.txt; done <patterns
