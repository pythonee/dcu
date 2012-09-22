gnufiles=`find . -type f -name "*.gnu"`  
for filename in $gnufiles
do
    gnuplot $filename
done

epsfiles=`find . -type f -name "*.eps"`

for filename in $epsfiles
do
    epstopdf $filename
done
