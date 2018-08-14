$machineName = "isolationlevels"

$dockerMachines = docker-machine ls 
if ($dockerMachines -Like "*$machineName*") {
	echo "Starting docker machine..."
	docker-machine start $machineName
	docker-machine env $machineName
	&docker-machine env $machineName | Invoke-Expression
	echo "Docker machine started."
} else {
	echo "Creating docker machine..." 
	docker-machine create --driver virtualbox --virtualbox-disk-size "2000" --virtualbox-memory "4096" --virtualbox-cpu-count "2" --virtualbox-hostonly-cidr "192.168.90.1/24" $machineName 
	docker-machine start $machineName 
	docker-machine env $machineName
	&docker-machine env $machineName | Invoke-Expression
	echo "Docker machine created."
	
    docker-machine start $machineName

	echo "Setting ports forwarding on Oracle VirtualBox machine..."
	&"C:\Program Files\Oracle\VirtualBox\VBoxManage" controlvm "$machineName" natpf1 "postgres,tcp,,5432,,5432"
	echo "Setting ports forwarding finished."

    docker-machine env $machineName
    &docker-machine env $machineName | Invoke-Expression
}