set key box
set key top right
set xlabel "Simulation Time"
set ylabel "Rate"
set yrange [0:2]
set terminal postscript eps enhanced color
set output "eps/scenario_1/udp/rate_bw.eps"
plot "../result/scenario_1/udp/ad.rate" using 1:2 with lines lw 3 lc rgb "red" title "adaptive rate", \
"../result/scenario_1/udp/hi.rate" using 1:2 with lines lw 3 lc rgb "orange" title "highest rate", \
"../result/scenario_1/udp/lo.rate" using 1:2 with lines lw 3 lc rgb "blue" title "lowest rate"
