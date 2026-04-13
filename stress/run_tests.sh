rm report.csv
rm -r result/
rm -r report/
jmeter -n -t loader.jmx -l result/results.csv -e -o report