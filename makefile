run:
	javac Mat.java
	javac EigenNewton.java
	javac Fixerupper.java
	javac Driver.java
	java Driver
test:
	javac Tester.java
	java Tester
	less Testfile.txt

convert:
	javac Fixerupper.java
	java Fixerupper