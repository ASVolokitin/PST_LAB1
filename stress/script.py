import os
import matplotlib.pyplot as plt

REPORT_FILE_PATH = "report.csv"

def find_max_user_amount(file_path):
    if not os.path.exists(file_path):
        return None
    with open(file_path, 'r') as f:
        lines = f.readlines()[1:]
    for line in lines:
        parts = line.strip().split(',')
        if len(parts) < 13:
            continue
        response_code = parts[3]
        grp_threads = parts[12]
        if response_code in ('503'):
            return int(grp_threads)
    return None

def plot_latency_vs_threads(file_path):
    if not os.path.exists(file_path):
        return None
    with open(file_path, 'r') as f:
        lines = f.readlines()[1:]
    
    threads_200 = []
    latencies_200 = []
    threads_500 = []
    latencies_500 = []
    threads_503 = []
    latencies_503 = []
    
    for line in lines:
        parts = line.strip().split(',')
        if len(parts) < 15:
            continue
        try:
            grp_threads = int(parts[12])
            latency = int(parts[14])
            response_code = parts[3]
            
            if response_code == '200':
                threads_200.append(grp_threads)
                latencies_200.append(latency)
            elif response_code == '500':
                threads_500.append(grp_threads)
                latencies_500.append(latency)
            elif response_code == '503':
                threads_503.append(grp_threads)
                latencies_503.append(latency)
        except ValueError:
            continue
    
    plt.figure(figsize=(10, 6))
    
    plt.scatter(threads_200, latencies_200, alpha=0.6, s=20, color='green', label='200 OK')
    plt.scatter(threads_500, latencies_500, alpha=0.6, s=20, color='gray', label='500 Error')
    plt.scatter(threads_503, latencies_503, alpha=0.6, s=20, color='red', label='503 Error')
    
    plt.xlabel('grpThreads')
    plt.ylabel('Latency (ms)')
    plt.title('Latency vs Number of Threads')
    plt.legend()
    plt.grid(True, alpha=0.3)
    
    # threshold = find_max_user_amount(REPORT_FILE_PATH)
    # plt.axvline(x=threshold, color='black', linestyle='-', linewidth=2)
    
    # max_x = max(threads_200 + threads_500 + threads_503) if (threads_200 + threads_500 + threads_503) else threshold
    # plt.axvspan(threshold, max_x, alpha=0.3, color='red')
    
    plt.show()

print(find_max_user_amount(REPORT_FILE_PATH))
plot_latency_vs_threads(REPORT_FILE_PATH)