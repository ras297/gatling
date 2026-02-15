echo "CPU Cores: $(nproc)"
echo "Memory: $(free -h | awk '/Mem:/ {print $2}')"
echo "Disk: $(df -h / | awk 'NR==2 {print $2 " total, " $4 " available"}')"