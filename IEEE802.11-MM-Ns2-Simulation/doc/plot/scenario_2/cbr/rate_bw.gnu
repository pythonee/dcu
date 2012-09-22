set key box
set key top right
set xlabel "Simulation Time"
set ylabel "Rate"
set yrange [0:2]
set terminal postscript eps enhanced color
set output "eps/scenario_2/cbr/rate_bw.eps"
plot "../result/scenario_2/cbr/ad.rate" using 1:2 with lines lw 3 lc rgb "red" title "adaptive rate", \
"../result/scenario_2/cbr/hi.rate" using 1:2 with lines lw 3 lc rgb "orange" title "highest rate", \
"../result/scenario_2/cbr/lo.rate" using 1:2 with lines lw 4 lc rgb "blue" title "lowest rate"
