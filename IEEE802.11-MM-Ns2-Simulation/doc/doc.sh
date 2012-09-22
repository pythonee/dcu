rm report.pdf
if [ -d "eps" ]; then
    rm -rf eps
fi

mkdir -p eps/scenario_1/cbr
mkdir -p eps/scenario_1/udp
mkdir -p eps/scenario_2/cbr
mkdir -p eps/scenario_2/udp

#dot plot/scenario_1/model.dot -Teps -o eps/scenario_1/model.eps

filelist=`find plot/scenario_1/ -type f -name "*.gnu"`  
for filename in $filelist
do
    gnuplot $filename
done

filelist=`find plot/scenario_2/ -type f -name "*.gnu"`  
for filename in $filelist
do
    gnuplot $filename
done

pdflatex report.tex 

rm *.log

rm eps/scenario_1/cbr/*.pdf
rm eps/scenario_1/udp/*.pdf
rm eps/scenario_2/cbr/*.pdf
rm eps/scenario_2/udp/*.pdf
