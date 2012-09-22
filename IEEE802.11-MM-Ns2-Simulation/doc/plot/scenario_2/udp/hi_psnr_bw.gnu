unset key
set xlabel "Batch"
set ylabel "PSNR"
set yrange [:110]
set terminal postscript eps enhanced color
set output "eps/scenario_2/udp/hi_psnr_bw.eps"
set title "highest data rate packet delay"
plot "../result/scenario_2/udp/hi.psnr" using 1:2 with lines lw 3 lc rgb "orange"
