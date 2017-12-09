function killitif {
    docker ps -a  > /tmp/yy_xx$$
    if grep --quiet $1 /tmp/yy_xx$$
     then
     echo "killing older version of $1"
     docker rm -f `docker ps -a | grep $1 | sed -e 's: .*$::'`
   fi
}


if [ $1 = "web1" ];
   then
   docker run -d -P --network=ecs189_default --name=$1 activity
   docker exec ecs189_proxy_1 /bin/bash /bin/swap1.sh
   killitif web2
fi

if [ $1 = "web2" ];
   then
   docker run -d -P --network=ecs189_default --name=$1 activity2
   docker exec ecs189_proxy_1 /bin/bash /bin/swap2.sh
   killitif web1
fi

echo "redirecting to the service" 
echo "...swap done!" 
