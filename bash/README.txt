Run ./run.sh to
- append key to dane in the first column
- append key to slownik in the first column
- join
- generate pairable.txt with matched data
- generate impairable.txt with the unmatched data
- query openstreetmap for lat/long
- query epsg.io for PL 2000 coords
- save in fix.txt, join to final.txt

Example output (end):

Ending time
Sun Oct 29 13:14:54 CET 2017
   11732 pairable.txt
     461 impairable.txt
   12154 matched.txt
      39 unmatched.txt

real	16m37.605s
user	0m37.256s
sys	0m24.915s

Results in matched.txt -- 99.68% matching rate.

./analyze.sh was used in the decelopment, not working, leaving just in case
