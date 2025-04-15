import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

# Load data from both CSV files
file_aodv = 'simulation_results.csv'  # Replace with the AODV file path
file_raodv = 'simulation_results2.csv'  # Replace with the RAODV file path

data_aodv = pd.read_csv(file_aodv)
data_raodv = pd.read_csv(file_raodv)

# Define a function to create pairwise plots


def plot_pairwise_graph(x_col, y_col, x_label, y_label, title, filename):
    plt.figure(figsize=(12, 8))

    # Group by the x_col and calculate the mean of y_col for both protocols
    avg_aodv = data_aodv.groupby(x_col)[y_col].mean().reset_index()
    avg_raodv = data_raodv.groupby(x_col)[y_col].mean().reset_index()

    # Plot both AODV and RAODV on the same graph
    plt.plot(avg_aodv[x_col], avg_aodv[y_col],
             marker='o', color='b', label='AODV')
    plt.plot(avg_raodv[x_col], avg_raodv[y_col],
             marker='s', color='r', label='RAODV')

    plt.xlabel(x_label)
    plt.ylabel(y_label)
    plt.title(title)
    plt.legend(loc='upper left')
    plt.grid(True)
    plt.tight_layout()
    plt.savefig(filename)
    plt.close()


# Create pairwise comparison plots
# 1. Number of Nodes vs Throughput
plot_pairwise_graph('Nodes', 'Throughput',
                    'Number of Nodes', 'Throughput',
                    'Throughput vs Number of Nodes', 'throughput_vs_nodes_comparison.png')

# 2. Number of Nodes vs Delay
plot_pairwise_graph('Nodes', 'EndToEndDelay',
                    'Number of Nodes', 'End to End Delay',
                    'Delay vs Number of Nodes', 'delay_vs_nodes_comparison.png')

# 3. Number of Nodes vs Packet Delivery Ratio
plot_pairwise_graph('Nodes', 'PacketDeliveryRatio',
                    'Number of Nodes', 'Packet Delivery Ratio (PDR)',
                    'PDR vs Number of Nodes', 'pdr_vs_nodes_comparison.png')

# 4. Number of Nodes vs Packet Drop Ratio
plot_pairwise_graph('Nodes', 'PacketDropRatio',
                    'Number of Nodes', 'Packet Drop Ratio',
                    'Packet Drop Ratio vs Number of Nodes', 'pd_ratio_vs_nodes_comparison.png')

# 5. Speed vs Throughput
plot_pairwise_graph('Speed', 'Throughput',
                    'Speed', 'Throughput',
                    'Throughput vs Speed', 'throughput_vs_speed_comparison.png')

# 6. Speed vs Delay
plot_pairwise_graph('Speed', 'EndToEndDelay',
                    'Speed', 'End to End Delay',
                    'Delay vs Speed', 'delay_vs_speed_comparison.png')

# 7. Speed vs Packet Delivery Ratio
plot_pairwise_graph('Speed', 'PacketDeliveryRatio',
                    'Speed', 'Packet Delivery Ratio (PDR)',
                    'PDR vs Speed', 'pdr_vs_speed_comparison.png')

# 8. Speed vs Packet Drop Ratio
plot_pairwise_graph('Speed', 'PacketDropRatio',
                    'Speed', 'Packet Drop Ratio',
                    'Packet Drop Ratio vs Speed', 'pd_ratio_vs_speed_comparison.png')

# 9. Packets Per Second (PPS) vs Throughput
plot_pairwise_graph('PacketsPerSecond', 'Throughput',
                    'Packets Per Second (PPS)', 'Throughput',
                    'Throughput vs PPS', 'throughput_vs_pps_comparison.png')

# 10. Packets Per Second (PPS) vs Delay
plot_pairwise_graph('PacketsPerSecond', 'EndToEndDelay',
                    'Packets Per Second (PPS)', 'End to End Delay',
                    'Delay vs PPS', 'delay_vs_pps_comparison.png')

# 11. Packets Per Second (PPS) vs Packet Delivery Ratio
plot_pairwise_graph('PacketsPerSecond', 'PacketDeliveryRatio',
                    'Packets Per Second (PPS)', 'Packet Delivery Ratio (PDR)',
                    'PDR vs PPS', 'pdr_vs_pps_comparison.png')

# 12. Packets Per Second (PPS) vs Packet Drop Ratio
plot_pairwise_graph('PacketsPerSecond', 'PacketDropRatio',
                    'Packets Per Second (PPS)', 'Packet Drop Ratio',
                    'Packet Drop Ratio vs PPS', 'pd_ratio_vs_pps_comparison.png')

print("Pairwise comparison graphs saved successfully!")
