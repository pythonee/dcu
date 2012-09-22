set key box
set key top left
set xtics nomirror
set ytics nomirror
 set grid 
set xlabel "Network Load Scale"
set ylabel "Burst Block Rate [ x1000]"
set terminal postscript eps enhanced color
set output "block_rate_avg.eps"
plot "without_cc/block_rate_avg.csv" using 1:2 with linespoints lt 1 lw 3 lc rgb "red" title "without congestion control", \
"with_cc_0_1/block_rate_avg.csv" using 1:2 with linespoints lt 1 lw 3 lc rgb "blue" title "threshold 0.10"
