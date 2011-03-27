pid=`ps -ax | grep vyuudha | awk '{print $1}'`

if [ "$pid" != "" ]
then
	kill $pid
	echo $(pid)': Vyuudha Stopped! '
	exit 0
fi