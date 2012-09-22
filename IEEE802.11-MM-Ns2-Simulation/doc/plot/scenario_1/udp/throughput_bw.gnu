set key box
set key top right
set xlabel "Simulation Time"
set ylabel "Throughpt"
set terminal postscript eps enhanced color
set output "eps/scenario_1/udp/throughput_bw.eps"
plot "../result/scenario_1/udp/ad.throughput" using 1:2 with lines lw 2 lc rgb "red" title "adaptive rate", \
"../result/scenario_1/udp/hi.throughput" using 1:2 with lines lw 2 lc rgb "orange" title "highest rate", \
"../result/scenario_1/udp/lo.throughput" using 1:2 with lines lw 4 lc rgb "blue" title "lowest rate"
