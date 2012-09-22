unset key
set xlabel "Batch"
set ylabel "Delay"
set terminal postscript eps enhanced color
set output "eps/scenario_1/cbr/ad_delay_bw.eps"
set title "Adaptive data rate packet delay"
plot "../result/scenario_1/cbr/ad.delay" using 1:2 with lines lw 3 lc rgb "red"
