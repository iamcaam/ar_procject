#!/bin/bash
clear_update_file()
{
	rm -rf /tmp/ThinClient
	rm -f /tmp/image.bin
}
echo 1 > update.txt
if [ -f /tmp/image.bin ]; then
	cd /tmp
	tar -zxvf /tmp/image.bin > /dev/null 2>&1
	if [ -f /tmp/JavaClient/install.sh ];  then
		result=`/tmp/ThinClient/install.sh`		
		if [ $result -eq 0 ]; then
			echo 0
			#reboot
		else 	
			echo -3
		fi
		clear_update_file
	else
		clear_update_file
		echo -2	
	fi 
else
	echo -1;
fi
