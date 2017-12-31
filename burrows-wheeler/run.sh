javac -cp "/home/lpang/Downloads/algs4.jar" BurrowsWheeler.java CircularSuffixArray.java MoveToFront.java
if [ $# -eq 3 ]
then
java -cp "/home/lpang/Downloads/algs4.jar:." $1 $2 < $3
elif [ $# -eq 2 ]
then
java -cp "/home/lpang/Downloads/algs4.jar:." $1 $2
elif [$# -eq 1 ]
then
java -cp "/home/lpang/Downloads/algs4.jar:." $1
fi
