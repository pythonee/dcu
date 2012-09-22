unset key
set xlabel "Batch"
set ylabel "Delay"
set terminal postscript eps enhanced color
set output "eps/scenario_1/cbr/lo_delay_bw.eps"
set title "Lowest data rate packet delay"
plot "../result/scenario_1/cbr/lo.delay" using 1:2 with lines lw 3 lc rgb "blue"
