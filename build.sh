#!/bin/bash
meta="META-INF"
command="Main-Class: src.PlanMyWeek"
jar cf parser.zip src/*
mkdir temp
for file in parser.zip
do
  unzip -P pcp9100 "$file" -d temp/
done
rm parser.zip
cd temp/$meta/
rm MANIFEST.MF
echo $command >> MANIFEST.MF
cd ../
zip -r ../parser.zip META-INF src
cd ../
rm -rf temp
mv parser.zip PlanMyWeek.jar
