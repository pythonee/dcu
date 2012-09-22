unset key
set xlabel "Batch"
set ylabel "Delay"
set terminal postscript eps enhanced color
set output "eps/scenario_2/cbr/hi_delay_bw.eps"
set title "highest data rate packet delay"
plot "../result/scenario_2/cbr/hi.delay" using 1:2 with lines lw 3 lc rgb "orange"
