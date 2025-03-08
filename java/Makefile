all: run

clean:
	rm -f out/PiEstimation.jar

out/PiEstimation.jar: out/parcs.jar src/PiEstimation.java
	@mkdir -p temp
	@javac -cp out/parcs.jar -d temp src/PiEstimation.java
	@jar cf out/PiEstimation.jar -C temp .
	@rm -rf temp/

build: out/PiEstimation.jar

run: out/PiEstimation.jar
    @cd out && java -cp 'parcs.jar:PiEstimation.jar' PiEstimation $(WORKERS) > ../sorted_output.txt
