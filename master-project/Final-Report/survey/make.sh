rm *.pdf
rm *.bbl
bibtex survey
make survey.pdf 
xdg-open survey.pdf
