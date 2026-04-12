rm -r report/
rm -r result/
jmeter -n -t loader.jmx -l result/results.csv -e -o report