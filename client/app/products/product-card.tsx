import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";

export default function ProductCard({ product }: { product: Product }) {
    return (
        <Card className="max-w-[480px]">
            <CardHeader className="h-28">
                <CardTitle>{product.name}</CardTitle>
                <CardDescription>{product.description}</CardDescription>
            </CardHeader>
            <CardContent className="h-24">
                <div>{product.imageUrl}</div>
                <div className="flex flex-wrap justify-between">
                    <div className="content-center">
                        <p className="inline font-bold h-[100%]">
                            {product.stock}
                        </p>{" "}
                        in stock
                    </div>
                    <div className="flex flex-wrap justify-end">
                        <p className="w-full text-end font-bold text-xl">
                            {(product.netPriceInCents / 100).toFixed(2)}$
                        </p>
                        <p className="w-full text-end font-light text-sm text-slate-500">
                            {(product.grossPriceInCents / 100).toFixed(2)}$ with
                            VAT
                        </p>
                    </div>
                </div>
            </CardContent>
            <CardFooter className="flex justify-end items-end text-sm text-slate-500">
                {product.barcode}
            </CardFooter>
        </Card>
    );
}
