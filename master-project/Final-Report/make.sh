rm *.aux *.log *.bbl *.blg

pdflatex final_report.tex
bibtex final_report.aux
pdflatex final_report.tex
pdflatex final_report.tex
pdflatex final_report_Fei_Wang.tex
