#!/bin/zsh

cd lab0

wc -l $(ls ../* */* */** **/*** 2>/tmp/oshibki| grep "e$" | sort -u) | cat>/tmp/results
ls -R clefable5/ 2>/dev/null | grep -v "/$"|sort 
cat clefable5/* 2>/dev/null| grep -vin "e$"
ls -l bulbasaur0/ | sort -k6
ls -Rl clefable5/* 2>/dev/null | sort -uk2r
ls bulbasaur0/ bulbasaur0/* bulbasaur0/*/* 2>tmp/oshibki6|grep -v "/"|sort -r