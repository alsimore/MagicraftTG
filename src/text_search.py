import os
from os import walk

count = 0
path = "."

for (dirpath, dirnames, filenames) in walk(path):
	for file in filenames:
		count += 1
		name = os.path.join(dirpath, file)
		with open(name) as fp:
			lines = fp.readlines()
			for line in lines:
				if "f3+" in line.lower():
					print name
					print line
					
print count