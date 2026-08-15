'use client';

import { DollarSign, TrendingUp, Wallet, Clock } from 'lucide-react';
import { formatCurrency, formatDate } from '@/lib/utils';

export default function VendorEarningsPage() {
  const stats = [
    { label: 'Total Earnings', value: '$12,450', icon: DollarSign, color: 'bg-green-500' },
    { label: 'Pending Payout', value: '$2,300', icon: Clock, color: 'bg-yellow-500' },
    { label: 'This Month', value: '$4,500', icon: TrendingUp, color: 'bg-blue-500' },
    { label: 'Available Balance', value: '$10,150', icon: Wallet, color: 'bg-purple-500' },
  ];

  const transactions = [
    { id: 1, type: 'Sale', order: 'ORD-001', amount: 179.98, date: '2024-03-15', status: 'COMPLETED' },
    { id: 2, type: 'Sale', order: 'ORD-002', amount: 99.99, date: '2024-03-20', status: 'COMPLETED' },
    { id: 3, type: 'Payout', amount: -500.00, date: '2024-03-25', status: 'COMPLETED' },
  ];

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Earnings</h1>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6 mb-8">
        {stats.map((stat) => {
          const Icon = stat.icon;
          return (
            <div key={stat.label} className="bg-white rounded-lg shadow-sm p-6">
              <div className="flex items-center">
                <div className={`${stat.color} p-3 rounded-lg`}>
                  <Icon className="h-6 w-6 text-white" />
                </div>
                <div className="ml-4">
                  <p className="text-sm text-gray-600">{stat.label}</p>
                  <p className="text-2xl font-bold">{stat.value}</p>
                </div>
              </div>
            </div>
          );
        })}
      </div>

      <div className="bg-white rounded-lg shadow-sm">
        <div className="p-6 border-b">
          <h2 className="text-xl font-semibold">Transaction History</h2>
        </div>
        <div className="p-6 border-b">
          <div className="grid grid-cols-5 gap-4 text-sm font-medium text-gray-600">
            <div>Type</div>
            <div>Order</div>
            <div>Date</div>
            <div>Amount</div>
            <div>Status</div>
          </div>
        </div>
        {transactions.map((transaction) => (
          <div key={transaction.id} className="p-6 border-b last:border-b-0">
            <div className="grid grid-cols-5 gap-4 items-center">
              <div className="font-medium">{transaction.type}</div>
              <div className="text-gray-600">{transaction.order || '-'}</div>
              <div className="text-gray-600">{formatDate(transaction.date)}</div>
              <div className={`font-semibold ${transaction.amount > 0 ? 'text-green-600' : 'text-red-600'}`}>
                {transaction.amount > 0 ? '+' : ''}{formatCurrency(transaction.amount)}
              </div>
              <div>
                <span className="px-2 py-1 rounded-full text-xs bg-green-100 text-green-800">
                  {transaction.status}
                </span>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
