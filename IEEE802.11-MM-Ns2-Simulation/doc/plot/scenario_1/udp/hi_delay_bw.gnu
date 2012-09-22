unset key
set xlabel "Batch"
set ylabel "Delay"
set terminal postscript eps enhanced color
set output "eps/scenario_1/udp/hi_delay_bw.eps"
set title "highest data rate packet delay"
plot "../result/scenario_1/udp/hi.delay" using 1:2 with lines lw 3 lc rgb "orange"
