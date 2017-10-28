echo Preparing dane
./dane.sh
echo Preparing slownik
./slownik.sh

echo Joining pairable
join -t";" -1 1 -2 1 dane2.txt slownik2.txt \
| tr -d '\r' \
> pairable.txt

echo Joining unpairable
join -t";" -1 1 -2 1 -v 1 dane2.txt slownik2.txt \
> impairable.txt

wc -l pairable.txt
wc -l impairable.txt
