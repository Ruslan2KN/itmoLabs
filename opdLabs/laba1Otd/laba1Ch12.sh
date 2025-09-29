#!/bin/zsh

mkdir lab0
cd lab0
touch bibarel5 
echo -e "Возможности Overland=8 Surface=9 Jump=3 Power=3\nIntelLigence=3 Fountain=0">bibarel5  
chmod 440 bibarel5 
mkdir -p bulbasaur0/froslass bulbasaur0/vileplume
cd bulbasaur0
chmod u=wx,g=wx,o=rx froslass
chmod 777 vileplume
touch glameow  
echo -e "Тип покемона NORMAL\nNONE" > glameow 
chmod 644 glameow 
cd ..
chmod 335 bulbasaur0
mkdir -p clefable5/victreebel clefable5/kadabra
cd clefable5
chmod 316 victreebel
chmod 711 kadabra
touch timburr  camerupt 
echo  "Тип покемона FIGHTING NONE">timburr 
echo -e "Развитые\nспособности Anger Point">camerupt 
chmod u=rw,g=w,o=w timburr 
chmod 044 camerupt 
cd ..
chmod u=rwx,g=wx,o=rw clefable5
touch machop2 
echo "Живет Cave Mountain">machop2 
chmod 624 machop2 
mkdir seaking8
cd seaking8
touch jynx  trubbish  primeape  glalie 
echo -e "Развитые\nспособности Hydration Dry Skin">jynx 
echo -e "Способности Venom Stench\nSticky Hold">trubbish 
echo -e "weigth=70.5 height=39.0 atk=11\ndef=6">primeape 
echo -e "Возможности Overland=8 Surface=3 Sky=6 Jump=3 Power4=0\nIntelligence=4 Chilled=0 Freezer=0">glalie 
chmod 444 jynx 
chmod 400 trubbish 
chmod 404 primeape 
chmod u=rw,g=,o= glalie 
cd ..
chmod 576 seaking8
touch snorlax4 
echo -e "weigth=1014.1 height=83.0\natk=11 def=7">snorlax4 
chmod 640 snorlax4 