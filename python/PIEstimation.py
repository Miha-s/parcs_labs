from Pyro4 import expose
import random
import time

class Solver:
    def __init__(self, workers=None, input_file_name=None, output_file_name=None):
        self.input_file_name = input_file_name
        self.output_file_name = output_file_name
        self.workers = workers
        print("Initialized Pi Estimation Solver")

    def solve(self):
        print("Pi Estimation Started")
        print("Workers: %d" % len(self.workers))
        self.total_darts = self.read_input()

        start_time = time.time()
        estimated_pi = self.parallel_estimate_pi()
        end_time = time.time()

        execution_time = end_time - start_time
        self.write_output(execution_time, estimated_pi)

    def parallel_estimate_pi(self):
        darts_per_process = self.total_darts // len(self.workers)
                    
        futures = [self.workers[i].estimate_pi_worker(darts_per_process) for i in range(len(self.workers))]
        results = [future.value for future in futures]
        total_hits = sum(results)

        return 4 * (float(total_hits) / self.total_darts)

    @staticmethod
    @expose
    def estimate_pi_worker(darts_per_process):
        hits = 0
        for _ in range(darts_per_process):
            x = random.uniform(-1, 1)
            y = random.uniform(-1, 1)
            if x**2 + y**2 <= 1:
                hits += 1
        return hits
    
    def read_input(self):
        with open(self.input_file_name, 'r') as f:
            line = f.readline().strip()
            return int(line)


    def write_output(self, execution_time, estimated_pi):
        with open(self.output_file_name, 'a') as f:
            f.write('Total Darts: {}\n'.format(self.total_darts))
            f.write('Execution Time: {:.6f} seconds\n'.format(execution_time))
            f.write('Estimated Pi: {:.8f}\n'.format(estimated_pi))
        print("Pi estimation complete. Execution time and result written to file.")
