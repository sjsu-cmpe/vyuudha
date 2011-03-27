pid=`ps -ax | grep InitDDS | awk '{print $1}'`

if [ "$pid" != "" ]
then
	kill $pid
	echo $(pid)': Vyuudha Stopped! '
	exit 0
fi