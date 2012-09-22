unset key
set xlabel "Batch"
set ylabel "PSNR"
set yrange [:110]
set terminal postscript eps enhanced color
set output "eps/scenario_1/cbr/lo_psnr_bw.eps"
set title "Lowest data rate PSNR"
plot "../result/scenario_1/cbr/lo.psnr" using 1:2 with lines lw 3 lc rgb "blue"
