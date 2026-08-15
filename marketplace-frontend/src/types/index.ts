// Auth Types
export interface User {
  id: number;
  publicId: string;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
  status: 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}

// Product Types
export interface Product {
  id: number;
  publicId: string;
  name: string;
  slug: string;
  description: string;
  price: number;
  compareAtPrice?: number;
  sku: string;
  status: 'DRAFT' | 'PENDING' | 'APPROVED' | 'REJECTED' | 'INACTIVE';
  vendorId: number;
  vendorName: string;
  categoryId: number;
  categoryName: string;
  images: ProductImage[];
  variants: ProductVariant[];
  stockQuantity: number;
  createdAt: string;
  updatedAt: string;
}

export interface ProductImage {
  id: number;
  url: string;
  altText: string;
  sortOrder: number;
}

export interface ProductVariant {
  id: number;
  name: string;
  sku: string;
  price: number;
  stockQuantity: number;
  attributes: Record<string, string>;
}

export interface Category {
  id: number;
  name: string;
  slug: string;
  description?: string;
  parentId?: number;
  children?: Category[];
}

// Cart Types
export interface Cart {
  id: number;
  items: CartItem[];
  totalItems: number;
  totalAmount: number;
}

export interface CartItem {
  id: number;
  productId: number;
  productName: string;
  productImage?: string;
  price: number;
  quantity: number;
  variantId?: number;
  variantName?: string;
}

// Order Types
export interface Order {
  id: number;
  publicId: string;
  orderNumber: string;
  status: OrderStatus;
  items: OrderItem[];
  totalAmount: number;
  shippingAddress: Address;
  createdAt: string;
  updatedAt: string;
}

export type OrderStatus =
  | 'PENDING'
  | 'CONFIRMED'
  | 'PROCESSING'
  | 'SHIPPED'
  | 'DELIVERED'
  | 'CANCELLED'
  | 'REFUNDED';

export interface OrderItem {
  id: number;
  productId: number;
  productName: string;
  productImage?: string;
  price: number;
  quantity: number;
  vendorId: number;
  vendorName: string;
}

// Vendor Types
export interface Vendor {
  id: number;
  publicId: string;
  storeName: string;
  storeSlug: string;
  description?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED' | 'SUSPENDED';
  commissionRate: number;
  totalEarnings: number;
  totalSales: number;
  rating: number;
  createdAt: string;
}

export interface VendorAnalytics {
  totalSales: number;
  totalOrders: number;
  averageOrderValue: number;
  conversionRate: number;
  topProducts: Product[];
  salesTrend: SalesData[];
}

export interface SalesData {
  date: string;
  amount: number;
  orders: number;
}

// Wishlist Types
export interface Wishlist {
  id: number;
  items: WishlistItem[];
  totalItems: number;
}

export interface WishlistItem {
  id: number;
  productId: number;
  productName: string;
  productImage?: string;
  price: number;
  addedAt: string;
}

// Address Types
export interface Address {
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
}

// API Response Types
export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
  timestamp: string;
}

export interface PagedResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  empty: boolean;
}

// Admin Types
export interface DashboardMetrics {
  totalUsers: number;
  totalVendors: number;
  totalProducts: number;
  totalOrders: number;
  totalRevenue: number;
  monthlyRevenue: number;
  pendingApprovals: number;
  activeVendors: number;
}

export interface CommissionRule {
  id: number;
  name: string;
  type: 'PERCENTAGE' | 'FIXED';
  value: number;
  minOrderAmount?: number;
  maxOrderAmount?: number;
  status: 'ACTIVE' | 'INACTIVE';
}
