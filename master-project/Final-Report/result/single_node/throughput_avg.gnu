set key box
set key bottom right
set xtics nomirror
set ytics nomirror
 set grid 
set xlabel "Network Load Scale"
set ylabel "Throughput [ x1000]"
set terminal postscript eps enhanced color
set output "throughput_avg.eps"
plot "without_cc/throughput_avg.csv" using 1:2 with linespoints lt 1 lw 3 lc rgb "red" title "without congestion control", \
"with_cc_0_0_6/throughput_avg.csv" using 1:2 with linespoints lt 1 lw 3 lc rgb "orange" title "threshold 0.06", \
"with_cc_0_0_8/throughput_avg.csv" using 1:2 with linespoints lt 1 lw 3 lc rgb "blue" title "threshold 0.08", \
"with_cc_0_1/throughput_avg.csv" using 1:2 with linespoints lt 1 lw 3 lc rgb "black" title "threshold 0.10"
