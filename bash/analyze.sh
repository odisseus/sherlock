wc -l unpairable.txt
awk -F";" '{print $1"\t\t\t"$3"\t"$4}' unpairable.txt | less
